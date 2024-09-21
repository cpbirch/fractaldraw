import kotlin.math.cos
import kotlin.math.sin

enum class Colour(val argb: ARGB) {
    RED(ARGB(0xFF, 0xFF, 0x00, 0x00)),
    GREEN(ARGB(0xFF, 0x00, 0xFF, 0x00)),
    BLUE(ARGB(0xFF, 0x00, 0x00, 0xFF)),
    GRAY(ARGB(0xFF, 0xAB, 0xAB, 0xAB)),
    BLACK(ARGB(0xFF, 0x00, 0x00, 0x00)),
    WHITE(ARGB(0xFF, 0xFF, 0xFF, 0xFF)),
}

data class ARGB(val A: Int, val R: Int, val G: Int, val B: Int) {
    fun toInt() = (R shl 24) + (G shl 16) + (B shl 8) + A
}

fun generateSHMPalette(size: Int): List<ARGB> {
    val palette = MutableList<ARGB>(size) {
        val blue = sin(Math.toRadians((it + 90).toDouble())) * 255
        val green = sin(Math.toRadians(it * 1.33 +45)) * 255 // 30 degree offset
        val red = sin(Math.toRadians(it * 1.41 +30)) * 255
//        println ("r: $red, g: $green, b: $blue")
        ARGB(255, if (red >= 0) red.toInt() else 0, if (green >= 0) green.toInt() else 0, if (blue >= 0) blue.toInt() else 0) }
    return palette
}
