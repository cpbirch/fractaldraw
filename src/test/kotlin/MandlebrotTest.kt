import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MandlebrotTest {

    @Test
    fun `should calculate never escapes (max iterations)`() {
        // given
        val m = Mandlebrot(128)

        // when
        val escapeIterations = m.iterationsToEscapeValue(0f, 0f) // escape assumes infinite value

        // then
        assertEquals(128, escapeIterations)
    }

    @Test
    fun `should quickly hit escape value (fewer iterations)`() {
        // given
        val m = Mandlebrot(128)

        // when
        val escapeIterations = m.iterationsToEscapeValue(-2f, 1.12f) // escape assumes infinite value
        println(escapeIterations)

        // then
        assertTrue(escapeIterations < 128, "$escapeIterations is not less than 128")
        assertEquals(escapeIterations, 1)
    }

    @Test
    fun `should slowly hit escape value (many iterations)`() {
        // given
        val m = Mandlebrot(128)

        // when
        val escapeIterations = m.iterationsToEscapeValue(-0.8f, 0.9f) // escape assumes infinite value
        println(escapeIterations)

        // then
        assertTrue(escapeIterations < 128, "$escapeIterations is not less than 1080")
        assertEquals(4, escapeIterations)
    }

    @Test
    fun `should calculate never escapes (max iterations) alt`() {
        // given
        val m = Mandlebrot(128)

        // when
        val escapeIterations = m.iterationsToEscapeVals(0f, 0f) // escape assumes infinite value

        // then
        assertEquals(128, escapeIterations)
    }

    @Test
    fun `should quickly hit escape value (fewer iterations) alt`() {
        // given
        val m = Mandlebrot(128)

        // when
        val escapeIterations = m.iterationsToEscapeVals(-2f, 1.12f) // escape assumes infinite value
        println(escapeIterations)

        // then
        assertTrue(escapeIterations < 128, "$escapeIterations is not less than 128")
        assertEquals(0, escapeIterations)
    }

    @Test
    fun `should slowly hit escape value (fewer iterations) alt`() {
        // given
        val m = Mandlebrot(128)

        // when
        val escapeIterations = m.iterationsToEscapeVals(-0.8f, 0.9f) // escape assumes infinite value
        println(escapeIterations)

        // then
        assertTrue(escapeIterations < 128, "$escapeIterations is not less than 128")
        assertEquals(3, escapeIterations)
    }

    @Test
    fun `should calculate a row of escape values`() {
        // given
        val m = Mandlebrot(128)
        val y = 0f
        val xStart = -2f
        val xEnd = 2f
        val rowLength = 12

        // when
        val escapeList = m.rowEscapeValues(xStart, xEnd, y, rowLength)

        // then
        assertEquals(12, escapeList.size)
        assertEquals(128, escapeList[0])
        assertEquals(128, escapeList[6])
        assertEquals(1, escapeList[11])
    }
    @Test
    fun `should calculate a different row of escape values`() {
        // given
        val m = Mandlebrot(128)
        val y = -1.12f
        val xStart = -2f
        val xEnd = 2f
        val rowLength = 10

        // when
        val escapeList = m.rowEscapeValues(xStart, xEnd, y, rowLength)

        // then
        assertEquals(10, escapeList.size)
        assertEquals(0, escapeList[0])
        assertEquals(3, escapeList[5])
        assertEquals(1, escapeList[9])
    }
}