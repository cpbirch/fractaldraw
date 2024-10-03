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
        -fr <fractal-right: 0.47>
        -ft <fractal-top: 1.12>
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
    val right = args.asFloat("-fr", 0.47f)
    val top = args.asFloat("-ft", 1.12f)

    val fp = FractalPlane(left, right, top, graphics2D.width, graphics2D.height, graphics2D.aspectRatio)
    val calculator = Mandlebrot(fp.MAX_I)
    val maxY = graphics2D.height / parallel
    println("maxY: $maxY")
    println("minRowBytes: ${graphics2D.minRowBytes()}")

    val timeSource = TimeSource.Monotonic
    val mark1 = timeSource.markNow()
    var mark2 = mark1

    var paletteBytes = (0..graphics2D.width)
        .flatMap { fp.colourPaletteBytes(it) }
        .take(fp.pixelWidth * 4) // 4 bytes per pixel
        .toList()
    var y = 0
    while (!graphics2D.isWindowClosing()) {
        if (y < maxY) {
//            println("Calculating row $y of $maxY")
            calculator.parallelEscapeColourBytesRows(fp, y, parallel).forEach { row ->
                val yByte = row.row * graphics2D.minRowBytes().toInt()
                var x = 0
                row.escapeVals.forEach {
                    graphics2D.pixels[yByte + x] = it
                    x++
                }
            }
            if (y < 20) {
                val yByte = y * graphics2D.minRowBytes().toInt()
                var x = 0
                paletteBytes.forEach {
                    graphics2D.pixels[yByte + x] = it
                    x++
                }
            }
            graphics2D.writePixels()
            y++
        } else if (mark2 == mark1) {
            mark2 = timeSource.markNow()
            println(mark2 - mark1)
        }

        graphics2D.pollEvents()
    }
    // Render loop
}

fun destroy(graphics2D: Graphics2D) {
    // Free the window callbacks and destroy the window
    graphics2D.destroyWindow()

    // Terminate GLFW and free the error callback
    glfwTerminate()
    glfwSetErrorCallback(null)?.free();
}