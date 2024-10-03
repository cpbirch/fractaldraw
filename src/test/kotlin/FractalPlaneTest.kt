import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class FractalPlaneTest {

    @Test
    fun `should return 1_12 for y = 0`() {
        val fp = FractalPlane()

        // when
        val y = fp.toFractalY(0)

        // then
        assertEquals(1.12f, y)
    }

    @Test
    fun `should return 1f for y = 0`() {
        val fp = FractalPlane(-1f,1f, 1f, 100, 75)

        // when
        val y = fp.toFractalY(0)

        // then
        assertEquals(1f, y)
    }

    @Test
    fun `should return -1f for y = 100`() {
        val fp = FractalPlane(-1f,1f, 1f, 100, 75)

        // when
        val y = fp.toFractalY(100)

        // then
        assertEquals(-1f, y)
    }

    @Test
    fun `should return 0f for y = 50`() {
        val fp = FractalPlane(-1f,1f, 1f, 100, 75)

        // when
        val y = fp.toFractalY(50)

        // then
        assertEquals(0f, y)
    }

    @Test
    fun `should return -1f to 1f for pixelCoord 0x0`() {
        val fp = FractalPlane(-1f,1f, 1f, 100, 75)

        // when
        val fractalCoord = fp.toFractalCoord(0 to 0)

        // then
        assertEquals(-1.0f, fractalCoord.left())
        assertEquals(1.0f, fractalCoord.top())
    }

    @Test
    fun `should return 1f to -1f for pixelCoord 100x100`() {
        val fp = FractalPlane(-1f,1f, 1f, 100, 75)

        // when
        val fractalCoord = fp.toFractalCoord(100 to 100)

        // then
        assertEquals(1.0f, fractalCoord.left())
        assertEquals(-1.0f, fractalCoord.top())
    }

    @Test
    fun `should return 0f to 0f for pixelCoord 50x50`() {
        val fp = FractalPlane(-1f,1f, 1f, 100, 75)

        // when
        val fractalCoord = fp.toFractalCoord(50 to 50)

        // then
        assertEquals(0f, fractalCoord.left())
        assertEquals(0f, fractalCoord.top())
    }

    @Test
    fun `should return 0 to 0 for fractalCoord -2 x 1_12`() {
        val fp = FractalPlane()

        // when
        val pixelCoord = fp.toPixelCoord(-2f to 1.12f)

        // then
        assertEquals(0, pixelCoord.x())
        assertEquals(0, pixelCoord.y())
    }

    @Test
    fun `should return 0x0 for fractalCoord -1f to 1f`() {
        val fp = FractalPlane(-1f,1f, 1f, 100, 75)

        // when
        val pixelCoord = fp.toPixelCoord(-1f to 1f)

        // then
        assertEquals(0, pixelCoord.x())
        assertEquals(0, pixelCoord.y())
    }

    @Test
    fun `should return 100x100 for fractalCoord 1f to -1f`() {
        val fp = FractalPlane(-1f,1f, 1f, 100, 75)

        // when
        val pixelCoord = fp.toPixelCoord(1f to -1f)

        // then
        assertEquals(100, pixelCoord.x())
        assertEquals(100, pixelCoord.y())
    }

    @Test
    fun `should return 50x50 for fractalCoord 0f to 0f`() {
        val fp = FractalPlane(-1f,1f, 1f, 100, 75)

        // when
        val pixelCoord = fp.toPixelCoord(0f to 0f)

        // then
        assertEquals(50, pixelCoord.x())
        assertEquals(50, pixelCoord.y())
    }

    @Test
    fun `should convert escapeVals sequence to RGBA byte sequence`() {
        // given
        val fp = FractalPlane(pixelWidth = 100, MAX_I = 128)
        val escapeVal = 128

        // when
        val rgbaList = fp.colourPaletteBytes(escapeVal)

        // then
        assertEquals(4, rgbaList.size)
        assertEquals(0, rgbaList[0])
        assertEquals(0, rgbaList[1])
        assertEquals(0, rgbaList[2])
        assertEquals(255.toByte(), rgbaList[3])
    }
}