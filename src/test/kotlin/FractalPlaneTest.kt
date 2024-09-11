import io.github.humbleui.types.Rect
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class FractalPlaneTest {

    @Test
    fun `should return a 0x0 grey pixel`() {
        val fp = FractalPlane()

        // when
        val paintedPixel = fp.colourAtPixel(0 to 0)

        // then
        assertEquals(0, paintedPixel.pixelCoord.x())
        assertEquals(0, paintedPixel.pixelCoord.y())
//        assertEquals(0xFFABABAB.toInt(), paintedPixel.colour)
    }

    @Test
    fun `should return a 20x20 grey pixel`() {
        val fp = FractalPlane()

        // when
        val paintedPixel = fp.colourAtPixel(20 to 20)

        // then
        assertEquals(20, paintedPixel.pixelCoord.x())
        assertEquals(20, paintedPixel.pixelCoord.y())
//        assertEquals(0xFFABABAB.toInt(), paintedPixel.colour)
    }

    @Test
    fun `should return a black pixel at midpoint`() {
        val fp = FractalPlane()

        // when
        val paintedPixel = fp.colourAtPixel(320 to 240)

        // then
        assertEquals(320, paintedPixel.pixelCoord.x())
        assertEquals(240, paintedPixel.pixelCoord.y())
        assertEquals(Colour.BLACK.argb, paintedPixel.colour)
    }

    @Test
    fun `should return -2_0 to 1_12 for pixelCoord 0x0`() {
        val fp = FractalPlane()

        // when
        val fractalCoord = fp.toFractalCoord(0 to 0)

        // then
        assertEquals(-2.0f, fractalCoord.left())
        assertEquals(1.12f, fractalCoord.top())
    }

    @Test
    fun `should return -1f to 1f for pixelCoord 0x0`() {
        val fp = FractalPlane(Rect(-1f,1f, 1f, -1f), 100, 100)

        // when
        val fractalCoord = fp.toFractalCoord(0 to 0)

        // then
        assertEquals(-1.0f, fractalCoord.left())
        assertEquals(1.0f, fractalCoord.top())
    }

    @Test
    fun `should return 1f to -1f for pixelCoord 100x100`() {
        val fp = FractalPlane(Rect(-1f,1f, 1f, -1f), 100, 100)

        // when
        val fractalCoord = fp.toFractalCoord(100 to 100)

        // then
        assertEquals(1.0f, fractalCoord.left())
        assertEquals(-1.0f, fractalCoord.top())
    }

    @Test
    fun `should return 0f to 0f for pixelCoord 50x50`() {
        val fp = FractalPlane(Rect(-1f,1f, 1f, -1f), 100, 100)

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
        val fp = FractalPlane(Rect(-1f,1f, 1f, -1f), 100, 100)

        // when
        val pixelCoord = fp.toPixelCoord(-1f to 1f)

        // then
        assertEquals(0, pixelCoord.x())
        assertEquals(0, pixelCoord.y())
    }

    @Test
    fun `should return 100x100 for fractalCoord 1f to -1f`() {
        val fp = FractalPlane(Rect(-1f,1f, 1f, -1f), 100, 100)

        // when
        val pixelCoord = fp.toPixelCoord(1f to -1f)

        // then
        assertEquals(100, pixelCoord.x())
        assertEquals(100, pixelCoord.y())
    }

    @Test
    fun `should return 50x50 for fractalCoord 0f to 0f`() {
        val fp = FractalPlane(Rect(-1f,1f, 1f, -1f), 100, 100)

        // when
        val pixelCoord = fp.toPixelCoord(0f to 0f)

        // then
        assertEquals(50, pixelCoord.x())
        assertEquals(50, pixelCoord.y())
    }

    @Test
    fun `should return black for a zero iteration`() {
        val fp = FractalPlane()

        // when
        val colour = fp.naiveRGBColour(512)

        // then
        assertEquals(0, colour.R)
        assertEquals(0, colour.G)
        assertEquals(0, colour.B)
    }

    @Test
    fun `should return white for a MAX_I iteration`() {
        val fp = FractalPlane()

        // when
        val colour = fp.naiveRGBColour(0)

        // then
        assertEquals(255, colour.R)
        assertEquals(255, colour.G)
        assertEquals(255, colour.B)
    }

    @Test
    fun `should return neither white or black for a median iteration length`() {
        val fp = FractalPlane()

        // when
        val colour = fp.naiveRGBColour(405)

        // then
        assertNotEquals(255, colour.R)
        assertNotEquals(255, colour.G)
        assertNotEquals(255, colour.B)
        assertNotEquals(0, colour.R)
        assertNotEquals(0, colour.G)
        assertNotEquals(0, colour.B)
    }
}