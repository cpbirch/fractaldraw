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
    val MAX_I: Int = 1080
) {
    val xScale: Float
    val yScale: Float
    val palette: List<ARGB>
    val pixelHeight: Int = (pixelWidth * 0.75).toInt() // preserve 4/3 ratio
    val rowStep = (bound.right - bound.left) / pixelWidth
    val b255 = 255.toByte()

    init {
        xScale = bound.width / pixelWidth
        yScale = bound.height / pixelHeight
        palette = generateSHMPalette(MAX_I)
    }

    fun red(index: Int) = palette[index].R.toByte()
    fun green(index: Int) = palette[index].G.toByte()
    fun blue(index: Int) = palette[index].B.toByte()

    fun rowEscapeValColourBytes(ev: Int): List<Byte> {
        return if (ev < MAX_I) listOf(red(ev), green(ev), blue(ev), b255)
        else listOf(0, 0, 0, b255)
    }

    fun colourPixel(fractalVal: Int, x: Int, y: Int, bytesPerRow: Int = pixelWidth * 4, pixels: ByteArray) {
        val argb = if (fractalVal < MAX_I) palette[fractalVal] else Colour.BLACK.argb
        val byte = (x * 4) + (y * bytesPerRow)
        pixels[byte] = argb.R.toByte()
        pixels[byte + 1] = argb.G.toByte()
        pixels[byte + 2] = argb.B.toByte()
        pixels[byte + 3] = argb.A.toByte()
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

