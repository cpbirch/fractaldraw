import io.github.humbleui.types.Rect
import kotlin.math.roundToInt

typealias PixelCoord = Pair<Int, Int> // x to y

fun PixelCoord.x() = this.first
fun PixelCoord.y() = this.second

typealias FractalCoord = Pair<Float, Float> // l to t

fun FractalCoord.left() = this.first
fun FractalCoord.top() = this.second


data class PaintedPixel(val pixelCoord: PixelCoord = 0 to 0, val colour: ARGB = Colour.GRAY.argb)

class FractalPlane(
    val fLeft: Float = -2f,
    val fRight: Float = 0.66f,
    val fTop: Float = 1.12f,
    val pixelWidth: Int = 640,
    val pixelHeight: Int = 480,
    val aspectRatio: Float = 0.75f,
    val MAX_I: Int = 1080
) {
    val fBottom: Float
    val bound: Rect
    val xScale: Float
    val yScale: Float
    val palette: List<ARGB>
    val b255 = 255.toByte()

    init {
        fBottom = fTop - ((fRight - fLeft) * aspectRatio)
        bound = Rect(fLeft, fTop, fRight, fBottom)
        xScale = bound.width / pixelWidth
        yScale = bound.height / pixelHeight
        palette = generateSHMPalette(MAX_I)
    }

    fun red(index: Int) = palette[index].R.toByte()
    fun green(index: Int) = palette[index].G.toByte()
    fun blue(index: Int) = palette[index].B.toByte()

    fun colourPaletteBytes(ev: Int): List<Byte> {
        return if (ev < MAX_I) listOf(red(ev), green(ev), blue(ev), b255)
        else listOf(0, 0, 0, b255)
    }

    fun toFractalY(y: Int): Float {
        return bound.top + y * yScale
    }

    fun toFractalCoord(pixelCoord: PixelCoord): FractalCoord {
//        println("bound.left: ${bound.left} + (pixelCoord.x(): ${pixelCoord.x()} * xScale: ${xScale})) to (bound.top: ${bound.top} - (pixelCoord.y(): ${pixelCoord.y()} * yScale: ${yScale})")
        return (bound.left + (pixelCoord.x() * xScale)) to (bound.top + (pixelCoord.y() * yScale))
    }

    fun toPixelCoord(fractalCoord: FractalCoord): PixelCoord {
        return ((fractalCoord.left() - bound.left) / xScale).roundToInt() to ((fractalCoord.top() - bound.top) / yScale).roundToInt()
    }
}

