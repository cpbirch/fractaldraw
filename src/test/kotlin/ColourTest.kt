import kotlin.test.Test
import kotlin.test.assertEquals

class ColourTest {

    @Test
    fun `should have 1080 ARGBs`() {
        // given

        // when
        val palette: List<ARGB> = generateSHMPalette(1080)

        // then
        assertEquals(1080, palette.size)
        assertEquals(255, palette[0].B)
        assertEquals(182, palette[0].G)
        assertEquals(159, palette[0].R)

        assertEquals(95, palette[540].B)
        assertEquals(231, palette[540].G)
        assertEquals(255, palette[540].R)
    }

    @Test
    fun `should have 360 ARGBs`() {
        // given

        // when
        val palette: List<ARGB> = generateSHMPalette(360)

        // then
        assertEquals(360, palette.size)
        assertEquals(255, palette[0].B)
        assertEquals(182, palette[0].G)
        assertEquals(159, palette[0].R)

        assertEquals(95, palette[180].B)
        assertEquals(231, palette[180].G)
        assertEquals(255, palette[180].R)
    }

    @Test
    fun `should have 500 ARGBs`() {
        // given

        // when
        val palette: List<ARGB> = generateSHMPalette(720)

        // then
        assertEquals(720, palette.size)
        assertEquals(255, palette[0].B)
        assertEquals(182, palette[0].G)
        assertEquals(159, palette[0].R)

        assertEquals(95, palette[360].B)
        assertEquals(231, palette[360].G)
        assertEquals(255, palette[360].R)
    }
}