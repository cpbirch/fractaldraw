import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import kotlin.time.TimeSource


fun main(args: Array<String>) {
    val arguments = Arguments(args)

    println(
        """
        -p <num-threads: 1>
        -w <pixels-width: 640>
        -fl <fractal-left: neg2.0>
        -fr <fractal-right: 0.66>
        -ft <fractal-top: 1.02>
        -ar <aspect-ratio: 0.75>
    """.trimIndent()
    )
    println("Program arguments: ${args.joinToString(" ")}")

    val graphics = init(arguments)
    loop(graphics, arguments)
    destroy(graphics)
}

fun init(args: Arguments): Graphics2D {
    val width = args.asInt("-w", 640)
    val aspectRatio = args.asFloat("-ar", 0.75f)

    val errorCallback: GLFWErrorCallback = GLFWErrorCallback.createPrint(System.err)
    glfwSetErrorCallback(errorCallback)

    // Initialise the GLFW
    if (!glfwInit()) {
        throw IllegalStateException("Unable to initialise GLFW")
    }

    val g2d = Graphics2D(width, aspectRatio, "Fractal Example")
    g2d.showWindow()
    return g2d
}

fun loop(graphics2D: Graphics2D, args: Arguments) {
    val parallel = args.asInt("-p", 1)
    val left = args.asFloat("-fl", -2f)
    val right = args.asFloat("-fr", 0.67f)
    val top = args.asFloat("-ft", 1f)

    var fp = FractalPlane(left, right, top, graphics2D.width, graphics2D.height, graphics2D.aspectRatio)
    val calculator = Mandlebrot(fp.MAX_I)
    val maxY = graphics2D.height / parallel

    val timeSource = TimeSource.Monotonic
    var mark1 = timeSource.markNow()
    var mark2 = mark1

    var y = 0
    while (!graphics2D.isWindowClosing()) { // Render loop aka game loop
        if (y < maxY) {
            calculator.parallelEscapeColourBytesRows(fp, y, parallel)
                .forEach { row ->
                    val yByte = row.row * graphics2D.minRowBytes().toInt()
                    var x = 0
                    row.escapeVals.forEach {
                        graphics2D.pixels[yByte + x] = it
                        x++
                    }
                }
            y++
        } else if (mark2 == mark1) { // is y >= maxY and time not recorded - done rendering
            mark2 = timeSource.markNow()
            println("Time taken: ${mark2 - mark1}")
        } else if (graphics2D.isZooming()) { // have we just zoomed in?
            fp = getZoomedInFractalPlane(graphics2D, fp) // get new fractal plane with zoomed in bounds
            y = 0
            mark1 = timeSource.markNow()
            mark2 = mark1
            graphics2D.resetZoom()
        }

        graphics2D.writePixels()
        graphics2D.pollEvents() // must be on main thread and not in a callback
    }
}

fun getZoomedInFractalPlane(graphics2D: Graphics2D, fp: FractalPlane): FractalPlane {
    val zoomBox = graphics2D.getZoomBox()
    val fTopLeft = fp.toFractalCoord(PixelCoord(zoomBox[0].toInt(), zoomBox[1].toInt()))
    val fBottomRight = fp.toFractalCoord(PixelCoord(zoomBox[2].toInt(), zoomBox[3].toInt()))
    println("Zoom to box: $zoomBox to fractal coords: $fTopLeft to $fBottomRight")
    return FractalPlane(
        fLeft = fTopLeft.x(),
        fRight = fBottomRight.x(),
        fTop = fTopLeft.y(),
        pixelWidth = graphics2D.width,
        pixelHeight = graphics2D.height,
        aspectRatio = graphics2D.aspectRatio,
        MAX_I = fp.MAX_I
    )
}

fun destroy(graphics2D: Graphics2D) {
    // Free the window callbacks and destroy the window
    graphics2D.destroyWindow()

    // Terminate GLFW and free the error callback
    glfwTerminate()
    glfwSetErrorCallback(null)?.free();
}