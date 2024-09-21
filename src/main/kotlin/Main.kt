import io.github.humbleui.skija.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import kotlin.time.TimeSource


data class Graphics2D(
    val window: Window,
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
    if (elem.startsWith("-")) Pair(map + (elem to emptyList()), elem)
    else Pair(map + (lastKey to map.getOrDefault(lastKey, emptyList()) + elem), lastKey)
}.first

fun main(args: Array<String>) {
    val argmap = parseArgs(args)

    println(
        """
        -p <num-threads>
        -w <pixels-width>
    """.trimIndent()
    )
    println("Program arguments: ${args.joinToString(" ")}")

    val graphics = init(argmap.get("-w")?.get(0)?.toInt() ?: 640)
    loop(graphics, argmap)
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
    // Create window
    val window = Window(width, height, "Fractal Example")

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

    window.showWindow()

    return Graphics2D(
        window,
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

fun loop(graphics2D: Graphics2D, argMap: Map<String, List<String>>) {
    val parallel = argMap["-p"]?.get(0)?.toInt() ?: 1
    val fp = FractalPlane(pixelWidth = graphics2D.width)
    var y = 0
    val calculator = Mandlebrot(fp.MAX_I)
    val maxY = fp.pixelHeight / parallel
    println("maxY: $maxY")
    println("minRowBytes: ${graphics2D.imageInfo.minRowBytes}")

    val timeSource = TimeSource.Monotonic
    val mark1 = timeSource.markNow()
    var mark2 = mark1

    while (!graphics2D.window.isClosing()) {
        if (y < maxY) {
            calculator.parallelEscapeColourBytesRows(fp, y, parallel).forEach { row ->
                val yByte = row.row * graphics2D.imageInfo.minRowBytes.toInt()
                var x = 0
                row.escapeVals.forEach {
                    graphics2D.pixels[yByte + x] = it
                    x++
                }
            }

            graphics2D.bitmap.installPixels(graphics2D.imageInfo, graphics2D.pixels, graphics2D.imageInfo.minRowBytes)
            graphics2D.bitmap.notifyPixelsChanged()
            graphics2D.surface.writePixels(graphics2D.bitmap, 0, 0)
            y++

            // DRAW HERE!!!
            graphics2D.context.flush()
            graphics2D.window.swapBuffer()
        } else if (mark2 == mark1) {
            mark2 = timeSource.markNow()
            println(mark2 - mark1)
        }

        graphics2D.window.pollEvents()
    }
    // Render loop
}

fun destroy(graphics2D: Graphics2D) {
    // Free the window callbacks and destroy the window
    graphics2D.window.destroy()

    // Terminate GLFW and free the error callback
    glfwTerminate()
    glfwSetErrorCallback(null)?.free();
}