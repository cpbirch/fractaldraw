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
        assertEquals(180, palette[0].G)
        assertEquals(127, palette[0].R)

        assertEquals(0, palette[90].B)
        assertEquals(67, palette[90].G)
        assertEquals(100, palette[90].R)

        assertEquals(0, palette[91].B)
        assertEquals(61, palette[91].G)
        assertEquals(94, palette[91].R)

        // Fix implementation - this should not be black
        assertEquals(0, palette[180].B)
        assertEquals(0, palette[180].G)
        assertEquals(0, palette[180].R)

        // Fix implementation - this should not be black
        assertEquals(0, palette[181].B)
        assertEquals(0, palette[181].G)
        assertEquals(0, palette[181].R)

        assertEquals(0, palette[270].B)
        assertEquals(177, palette[270].G)
        assertEquals(197, palette[270].R)

        assertEquals(255, palette[360].B)
        assertEquals(71, palette[360].G)
        assertEquals(10, palette[360].R)
    }
}