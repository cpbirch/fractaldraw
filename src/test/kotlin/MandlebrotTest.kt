import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MandlebrotTest {

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
        val escapeList = m.rowEscapeValuesAsList(xStart, xEnd, y, rowLength)

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
        val escapeList = m.rowEscapeValuesAsList(xStart, xEnd, y, rowLength)

        // then
        assertEquals(10, escapeList.size)
        assertEquals(0, escapeList[0])
        assertEquals(3, escapeList[5])
        assertEquals(1, escapeList[9])
    }

    @Test
    fun `should calculate row 10 of 10 escape values`() {
        // given
        val fp = FractalPlane(pixelWidth = 10, pixelHeight = 10, MAX_I = 128)
        val m = Mandlebrot(fp.MAX_I)
        val row = 9

        // when
        val escapeList = m.rowEscapeValuesAsList(fp, row)

        // then
        assertEquals(10, escapeList.size)
        assertEquals(0, escapeList[0])
        assertEquals(3, escapeList[5])
        assertEquals(3, escapeList[9])
    }
    @Test
    fun `should calculate two evenly distributed rows of escape values`() {
        // given
        val fp = FractalPlane(pixelWidth = 10, pixelHeight = 10, MAX_I = 128)
        val m = Mandlebrot(fp.MAX_I)
        val row = 0
        val parallelRows = 2

        // when
        val escapeRowList = m.rowParallelEscapeValuesAsList(fp, row, parallelRows)

        // then
        assertEquals(2, escapeRowList.size)
        assertEquals(0, escapeRowList[0].row)
        assertEquals(5, escapeRowList[1].row)
        assertEquals(2, escapeRowList[0].escapeVals[5])
        assertEquals(2, escapeRowList[0].escapeVals[9])
    }

    @Test
    fun `should calculate 8 evenly distributed rows of escape values`() {
        // given
        val fp = FractalPlane(pixelWidth = 10, pixelHeight = 16, MAX_I = 128)
        val m = Mandlebrot(fp.MAX_I)
        val row = 0
        val parallelRows = 8

        // when
        val escapeRowList = m.rowParallelEscapeValuesAsList(fp, row, parallelRows)

        // then
        assertEquals(8, escapeRowList.size)
        assertEquals(0, escapeRowList[0].row)
        assertEquals(2, escapeRowList[1].row)
        assertEquals(4, escapeRowList[2].row)
        assertEquals(6, escapeRowList[3].row)
        assertEquals(14, escapeRowList[7].row)
        assertEquals(2, escapeRowList[0].escapeVals[5])
        assertEquals(2, escapeRowList[0].escapeVals[9])
    }
}
