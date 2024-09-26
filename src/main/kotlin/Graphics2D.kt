import io.github.humbleui.skija.*

class Graphics2D(val width: Int = 640, val aspectRatio: Float = 0.75f, title: String) {
    val height: Int = (width * aspectRatio).toInt()
    val window = Window(width, height, title)

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

    val imageInfo = ImageInfo(width, height, ColorType.RGBA_8888, ColorAlphaType.PREMUL)
    val bitmap = Bitmap()
    var pixels = ByteArray((imageInfo.minRowBytes * height).toInt()) { 0 }

    init {
        bitmap.allocPixelsFlags(imageInfo, true)
    }

    fun minRowBytes() = imageInfo.minRowBytes

    fun showWindow() = window.showWindow()

    fun pollEvents() = window.pollEvents()

    fun destroyWindow() = window.destroy()

    fun isWindowClosing() = window.isClosing()

    fun writePixels() {
        bitmap.installPixels(imageInfo, pixels, imageInfo.minRowBytes)
        bitmap.notifyPixelsChanged()
        surface.writePixels(bitmap, 0, 0)

        // DRAW HERE!!!
        context.flush()
        window.swapBuffer()
    }
}