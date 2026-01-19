import kotlin.math.sin

data class ARGB(val A: Int, val R: Int, val G: Int, val B: Int) {
    fun toInt() = (R shl 24) + (G shl 16) + (B shl 8) + A
}

fun generateSHMPalette(size: Int) = MutableList(size) { it ->
    val blue = sinusoidalColourVariance(size, it, 720.0)
        .let { normalize(it, 50.0, 255.0) }.toInt()
    val green = sinusoidalColourVariance(size, it, 540.0)
        .let { normalize(it, 25.0, 230.0) }.toInt()
    val red = sinusoidalColourVariance(size, it, 360.0)
        .let { normalize(it, 25.0, 205.0) }.toInt()

    // println ("idx: $it, r: $red, g: $green, b: $blue")
    ARGB(255, red, green, blue)
}

fun sinusoidalColourVariance(totalColours: Int, colourIndex: Int, periodicity: Double) = (totalColours / periodicity)
    .let { colourIndex / it }
    .let { Math.toRadians(it) }
    .let { sin(it) }

fun normalize(
    value: Double,
    newMin: Double = 0.3,
    newMax: Double = 1.0,
    oldMin: Double = -1.0,
    oldMax: Double = 1.0,
) = ((value - oldMin) / (oldMax - oldMin)) * (newMax - newMin) + newMin


