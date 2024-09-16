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
        assertEquals(0, palette[0].G)
        assertEquals(0, palette[0].R)

        assertEquals(0, palette[90].B)
        assertEquals(127, palette[90].G)
        assertEquals(0, palette[90].R)

        assertEquals(0, palette[91].B)
        assertEquals(131, palette[91].G)
        assertEquals(0, palette[91].R)

        assertEquals(0, palette[180].B)
        assertEquals(220, palette[180].G)
        assertEquals(127, palette[180].R)

        assertEquals(0, palette[181].B)
        assertEquals(218, palette[181].G)
        assertEquals(131, palette[181].R)

        assertEquals(0, palette[270].B)
        assertEquals(0, palette[270].G)
        assertEquals(220, palette[270].R)

        assertEquals(255, palette[360].B)
        assertEquals(0, palette[360].G)
        assertEquals(0, palette[360].R)
    }
}