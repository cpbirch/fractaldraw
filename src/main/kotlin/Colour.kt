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

const val sixthPI = Math.PI / 6
const val halfPI = Math.PI / 2

fun generateSHMPalette(size: Int) = MutableList(size) {
    // 2Pi radians = 360
    // This function assumes a list size of 3 * 360 = 1080
    // The colour component values follow a sine wave
    // Normalise to 360 degrees / 2Pi radians
    val factor = size / 360
    val rad = Math.toRadians(it.toDouble() / factor) // stop the palette from cycling 3 times
    val blue = (sin(halfPI + rad) * 160) / 2 + 175 // 0rad = 255, PIrad = 160
    val green = (sin(11 * sixthPI + rad) * 96) / 2 + 207 // 0rad = 182, PIrad ~=232
    val red = (sin(rad - halfPI) * 96) / 2 + 207 // 0rad = 160,, PIrad = 255
    // println ("r: $red, g: $green, b: $blue")
    ARGB(
        255,
        if (red >= 0) red.toInt() else 0,
        if (green >= 0) green.toInt() else 0,
        if (blue >= 0) blue.toInt() else 0
    )
}

