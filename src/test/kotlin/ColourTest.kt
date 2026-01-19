import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ColourTest {

    @Test
    fun `should have 1080 ARGBs`() {
        // given

        // when
        val palette: List<ARGB> = generateSHMPalette(1080)

        // then
        assertEquals(1080, palette.size)

        assertTrue(palette.minOf { it.R } >= 25, "Red min value should be greater or equal to 25")
        assertTrue(palette.minOf { it.G } >= 25, "Green min value should be greater or equal to 25")
        assertTrue(palette.minOf { it.B } >= 50, "Blue min value should be greater or equal to 50")

        assertTrue(palette.maxOf { it.R } <= 205, "Red max value should be fewer or equal to 205")
        assertTrue(palette.maxOf { it.G } <= 230, "Green max value should be fewer or equal to 230")
        assertTrue(palette.maxOf { it.B } <= 255, "Blue max value should be fewer or equal to 255")
    }

    @Test
    fun `should have 360 ARGBs`() {
        // given

        // when
        val palette: List<ARGB> = generateSHMPalette(360)

        // then
        assertEquals(360, palette.size)

        assertTrue(palette.minOf { it.R } >= 25, "Red min value should be greater or equal to 25")
        assertTrue(palette.minOf { it.G } >= 25, "Green min value should be greater or equal to 25")
        assertTrue(palette.minOf { it.B } >= 50, "Blue min value should be greater or equal to 50")

        assertTrue(palette.maxOf { it.R } <= 205, "Red max value should be fewer or equal to 205")
        assertTrue(palette.maxOf { it.G } <= 230, "Green max value should be fewer or equal to 230")
        assertTrue(palette.maxOf { it.B } <= 255, "Blue max value should be fewer or equal to 255")

    }

    @Test
    fun `should have 720 ARGBs`() {
        // given

        // when
        val palette: List<ARGB> = generateSHMPalette(720)

        // then
        assertEquals(720, palette.size)

        assertTrue(palette.minOf { it.R } >= 25, "Red min value should be greater or equal to 25")
        assertTrue(palette.minOf { it.G } >= 25, "Green min value should be greater or equal to 25")
        assertTrue(palette.minOf { it.B } >= 50, "Blue min value should be greater or equal to 50")

        assertTrue(palette.maxOf { it.R } <= 205, "Red max value should be fewer or equal to 205")
        assertTrue(palette.maxOf { it.G } <= 230, "Green max value should be fewer or equal to 230")
        assertTrue(palette.maxOf { it.B } <= 255, "Blue max value should be fewer or equal to 255")

    }
}