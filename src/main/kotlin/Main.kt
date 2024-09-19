import io.github.humbleui.skija.*
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.*
import org.lwjgl.system.MemoryUtil.NULL
import kotlin.time.TimeSource


data class Graphics2D(
    val window: Long,
    val errorCallback: GLFWErrorCallback,
    val context: DirectContext,
    val renderTarget: BackendRenderTarget,
    val surface: Surface,
    val canvas: Canvas,
    val bitmap: Bitmap,
    val pixels: ByteArray,
    val imageInfo: ImageInfo,
    val width: Int = 640,
    val height: Int = 480
)

fun parseArgs(args: Array<String>) = args.fold(Pair(emptyMap<String, List<String>>(), "")) { (map, lastKey), elem ->
        if (elem.startsWith("-"))  Pair(map + (elem to emptyList()), elem)
        else Pair(map + (lastKey to map.getOrDefault(lastKey, emptyList()) + elem), lastKey)
    }.first

fun main(args: Array<String>) {
    val argmap = parseArgs(args)

    println("""
        -p <num-threads>
        -w <pixels-width>
    """.trimIndent())
    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString(" ")}")

    val graphics = init(argmap.get("-w")?.get(0)?.toInt() ?: 640)
    loop(graphics, argmap.get("-p")?.get(0)?.toInt() ?: 1)
    destroy(graphics)
}

fun init(width: Int = 640): Graphics2D {
    val height: Int = (width * 0.75).toInt()
    val errorCallback: GLFWErrorCallback = GLFWErrorCallback.createPrint(System.err)
    glfwSetErrorCallback(errorCallback)

    // Initialise the GLFW
    if (!glfwInit()) {
        throw IllegalStateException("Unable to initialise GLFW")
    }

    val title = "Fractal Draw"

    // Create window
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE)
    val windowHandle: Long = glfwCreateWindow(width, height, title, NULL, NULL)
    glfwMakeContextCurrent(windowHandle)
    glfwSwapInterval(1) // Enable v-sync... is this how many draws to swap the buffer?
    glfwShowWindow(windowHandle) // Shows the window

    // Initialize OpenGL
    // Do once per app launch
    GL.createCapabilities()

    // Create Skia OpenGL context
    // Do once per app launch
    val context = DirectContext.makeGL()

    // Create render target, surface and retrieve canvas from it
    // .close() and recreate on window resize
    val renderTarget = BackendRenderTarget.makeGL(
        width,
        height,  /*samples*/
        0,  /*stencil*/
        8,
        0,
        FramebufferFormat.GR_GL_RGBA8
    )

    // .close() and recreate on window resize
    val surface = Surface.wrapBackendRenderTarget(
        context,
        renderTarget,
        SurfaceOrigin.BOTTOM_LEFT,
        SurfaceColorFormat.RGBA_8888,
        ColorSpace.getSRGBLinear()
    )

    // do not .close() — Surface manages its lifetime here
    val canvas: Canvas = surface.getCanvas()
    val imageInfo = ImageInfo(width, height, ColorType.RGBA_8888, ColorAlphaType.PREMUL)
    val bitmap = Bitmap()
    bitmap.allocPixelsFlags(imageInfo, true)
    val pixels = ByteArray((imageInfo.minRowBytes * height).toInt()) { 0 }

    return Graphics2D(
        windowHandle,
        errorCallback,
        context,
        renderTarget,
        surface,
        canvas,
        bitmap,
        pixels,
        imageInfo,
        width,
        height
    )
}

fun loop(graphics2D: Graphics2D, parallel: Int = 1) {
    // val blueish = Paint().apply { setColor(0xFF3344AA.toInt()) }
    val fp = FractalPlane(pixelWidth = graphics2D.width, pixelHeight = graphics2D.height)
    var y = 0
    val calculator = Mandlebrot(fp.MAX_I)
    val maxY = fp.pixelHeight / parallel
    println("maxY: $maxY")

    val timeSource = TimeSource.Monotonic
    val mark1 = timeSource.markNow()
    var mark2 = mark1

    while (!glfwWindowShouldClose(graphics2D.window)) {
        // canvas.drawPoint(x, y, paint) // doesn't seem to draw anything
        // canvas.drawRect(Rect.makeXYWH(x,y,2f,2f), paint) // 1x1 size renders weird

        if (y < maxY) {
            calculator.rowParallelEscapeValuesAsList(fp, y, parallel)
                .forEach {row ->
                    var x = 0
                    row.escapeVals.forEach {
                        fp.colourPixel(it, x, row.row, graphics2D.imageInfo.minRowBytes.toInt(), graphics2D.pixels)
                        x++
                    }
                }

            graphics2D.bitmap.installPixels(graphics2D.imageInfo, graphics2D.pixels, graphics2D.imageInfo.minRowBytes)
            graphics2D.bitmap.notifyPixelsChanged()
            // val image = Image.makeRasterFromBitmap(graphics2D.bitmap.setImmutable())
            // canvas.drawImage(image, 0f, 0f)
            graphics2D.surface.writePixels(graphics2D.bitmap, 0, 0)
            y++

            // DRAW HERE!!!
            graphics2D.context.flush()
            glfwSwapBuffers(graphics2D.window) // wait for v-sync
        } else if (mark2 == mark1) {
            mark2 = timeSource.markNow()
            println(mark2 - mark1)
        }

        glfwPollEvents()
    }
    // Render loop
}

fun destroy(graphics2D: Graphics2D) {
    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(graphics2D.window)
    glfwDestroyWindow(graphics2D.window)

    // Terminate GLFW and free the error callback
    glfwTerminate()
    glfwSetErrorCallback(null)?.free();
}