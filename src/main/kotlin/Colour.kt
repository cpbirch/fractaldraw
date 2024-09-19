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

data class HSV(val H: Float, val S: Float, val V: Float) {
    fun toARGB(): ARGB {
        if (S == 0f) {
            val scaled = (V * 255).toInt()
            return ARGB(255, scaled, scaled, scaled)
        } else {
            val h6 = H * 6
            val i = if (h6 < 6f) h6.toInt() else 0
            val one = V * (1 - S)
            val two = V * (1 - S * (h6 - i))
            val three = V * (1 - S * (1 - (h6 - i)))

            return when (i) {
                0 -> ARGB(255, (V * 255).toInt(), (three * 255).toInt(), (one * 255).toInt())
                1 -> ARGB(255, (two * 255).toInt(), (V * 255).toInt(), (one * 255).toInt())
                2 -> ARGB(255, (one * 255).toInt(), (V * 255).toInt(), (three * 255).toInt())
                3 -> ARGB(255, (one * 255).toInt(), (two * 255).toInt(), (V * 255).toInt())
                4 -> ARGB(255, (three * 255).toInt(), (one * 255).toInt(), (V * 255).toInt())
                else -> ARGB(255, (V * 255).toInt(), (one * 255).toInt(), (two * 255).toInt())
            }
        }
    }
}

fun generateSHMPalette(size: Int): List<ARGB> {
    val palette = MutableList<ARGB>(1080) {
        val blue = sin(Math.toRadians((it + 90).toDouble())) * 255
        val green = sin(Math.toRadians(it * 1.33 +45)) * 255 // 30 degree offset
        val red = sin(Math.toRadians(it * 1.41 +30)) * 255
//        println ("r: $red, g: $green, b: $blue")
        ARGB(255, if (red >= 0) red.toInt() else 0, if (green >= 0) green.toInt() else 0, if (blue >= 0) blue.toInt() else 0) }
    return palette
}
