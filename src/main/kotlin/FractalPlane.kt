import io.github.humbleui.types.Rect
import kotlin.math.pow
import kotlin.math.roundToInt

typealias PixelCoord = Pair<Int, Int> // x to y

fun PixelCoord.x() = this.first
fun PixelCoord.y() = this.second

typealias FractalCoord = Pair<Float, Float> // l to t

fun FractalCoord.left() = this.first
fun FractalCoord.top() = this.second



data class PaintedPixel(val pixelCoord: PixelCoord = 0 to 0, val colour: ARGB = Colour.GRAY.argb)

class FractalPlane(
    val bound: Rect = Rect(-2.00f, 1.12f, 0.47f, -1.12f),
    val pixelWidth: Int = 640,
    val pixelHeight: Int = 480
) {
    val MAX_I = 1080
    val xScale: Float
    val yScale: Float
    val palette: List<ARGB>

    init {
        xScale = bound.width / pixelWidth
        yScale = bound.height / pixelHeight
        palette = generateSHMPalette(MAX_I)
    }

    fun colourAtPixel(pixelCoord: PixelCoord): PaintedPixel {
        var fc = toFractalCoord(pixelCoord)
        var x2 = 0f // x2:= 0
        var y2 = 0f // y2:= 0
        var x = 0.0f
        var y = 0.0f
        var iteration = 0

        while ((x2 + y2) <= 4 && (iteration < MAX_I)) { //while (x2 + y2 ≤ 4 and iteration < max_iteration) do
            y = 2 * x * y + fc.top() //    y:= 2 * x * y + y0
            x = x2 - y2 + fc.left() //    x:= x2 - y2 + x0
            x2 = x * x //    x2:= x * x
            y2 = y * y //    y2:= y * y
            iteration++ //    iteration:= iteration + 1
        }
//        iteration = pixelCoord.x() * pixelCoord.y() % MAX_I
//        println("iteration: $iteration")
        return if (iteration < MAX_I) PaintedPixel(pixelCoord, palette[iteration])
        else PaintedPixel(pixelCoord, Colour.BLACK.argb)
    }

    fun toFractalCoord(pixelCoord: PixelCoord): FractalCoord {
//        println("bound.left: ${bound.left} + (pixelCoord.x(): ${pixelCoord.x()} * xScale: ${xScale})) to (bound.top: ${bound.top} - (pixelCoord.y(): ${pixelCoord.y()} * yScale: ${yScale})")
        return (bound.left + (pixelCoord.x() * xScale)) to (bound.top + (pixelCoord.y() * yScale))
    }

    fun toPixelCoord(fractalCoord: FractalCoord): PixelCoord {
        return ((fractalCoord.left() - bound.left) / xScale).roundToInt() to ((fractalCoord.top() - bound.top) / yScale).roundToInt()
    }
}