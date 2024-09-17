import io.github.humbleui.skija.*
import io.github.humbleui.types.Rect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.*
import org.lwjgl.system.MemoryUtil.NULL
import kotlin.concurrent.timer


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

fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")

    val graphics = init()
    loop(graphics)
    destroy(graphics)
}

fun init(): Graphics2D {
    val errorCallback: GLFWErrorCallback = GLFWErrorCallback.createPrint(System.err)
    glfwSetErrorCallback( errorCallback )

    // Initialise the GLFW
    if ( ! glfwInit() ) {
        throw IllegalStateException("Unable to initialise GLFW")
    }

    val width = 640
    val height = 480
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
    val imageInfo: ImageInfo = ImageInfo(width, height, ColorType.RGBA_8888, ColorAlphaType.PREMUL)
    val bitmap: Bitmap = Bitmap()
    bitmap.allocPixelsFlags(imageInfo, true)
    val pixels = ByteArray((imageInfo.minRowBytes * height).toInt()) { 0 }

    return Graphics2D(windowHandle, errorCallback, context, renderTarget, surface, canvas, bitmap, pixels, imageInfo, width, height)
}

fun loop(graphics2D: Graphics2D) {
    // val blueish = Paint().apply { setColor(0xFF3344AA.toInt()) }
    val fp = FractalPlane(pixelWidth = graphics2D.width, pixelHeight = graphics2D.height)
    var x = 0
    var y = 0
    var bytePos = 0
    val calculator = Mandlebrot(1080)

    // Render loop
    while (!glfwWindowShouldClose(graphics2D.window)) {
        // canvas.drawPoint(x, y, paint) // doesn't seem to draw anything
        // canvas.drawRect(Rect.makeXYWH(x,y,2f,2f), paint) // 1x1 size renders weird
        if (y < fp.pixelHeight) {
            val row = calculator.rowEscapeValues(
                fp.bound.left,
                fp.bound.right,
                fp.toFractalCoord(x to y).top(),
                fp.pixelWidth
            )
            row.forEach {
                val argb = if (it < 1080) fp.palette[it] else Colour.BLACK.argb
                val byte = (x * 4) + (y * graphics2D.imageInfo.minRowBytes.toInt())
                graphics2D.pixels[byte] = argb.R.toByte()
                graphics2D.pixels[byte + 1] = argb.G.toByte()
                graphics2D.pixels[byte + 2] = argb.B.toByte()
                graphics2D.pixels[byte + 3] = argb.A.toByte()
                x++
            }

            graphics2D.bitmap.installPixels(graphics2D.imageInfo, graphics2D.pixels, graphics2D.imageInfo.minRowBytes)
            graphics2D.bitmap.notifyPixelsChanged()
            // val image = Image.makeRasterFromBitmap(graphics2D.bitmap.setImmutable())
            // canvas.drawImage(image, 0f, 0f)
            graphics2D.surface.writePixels(graphics2D.bitmap, 0, 0)
            x = 0
            y++
        }
//            println("line $y done")

        // DRAW HERE!!!
        graphics2D.context.flush()
        glfwSwapBuffers(graphics2D.window) // wait for v-sync
        glfwPollEvents()
    }
}

private fun update(graphics2D: Graphics2D, fractalPlane: FractalPlane) {

}

fun destroy(graphics2D: Graphics2D) {
    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(graphics2D.window)
    glfwDestroyWindow(graphics2D.window)

    // Terminate GLFW and free the error callback
    glfwTerminate()
    glfwSetErrorCallback(null)?.free();
}