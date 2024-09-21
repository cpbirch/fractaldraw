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
    fun `should calculate a sequence of escape values`() {
        // given
        val fp = FractalPlane(pixelWidth = 22, MAX_I = 128)
        val m = Mandlebrot(fp.MAX_I)
        val row = 0

        // when
        val escapeRowList = m.rowEscapeValues(fp.bound.left, fp.toFractalY(row), fp.rowStep).take(22).toList()

        escapeRowList.forEach { println("Value: $it") }

        // then
        assertEquals(0, escapeRowList.first()) // every 4th byte should be 255
        assertEquals(1, escapeRowList[5]) // every 4th byte should be 255
    }

    @Test
    fun `should calculate a row of escape value colour bytes`() {
        // given
        val fp = FractalPlane(pixelWidth = 22, MAX_I = 128)
        val m = Mandlebrot(fp.MAX_I)
        val row = 0

        // when
        val escapeRowList = m.rowEscapeColourBytes(fp, row)

        escapeRowList.take(20).chunked(4).forEach { println("Colour: $it") }

        // then
        assertEquals(88, escapeRowList.size)
        assertEquals(127.toByte(), escapeRowList[0]) // pixel1, some red
        assertEquals(255.toByte(), escapeRowList[2]) // pixel1, all the blue
        assertEquals(132.toByte(), escapeRowList[20]) // pixel5, a bit more red
        assertEquals(254.toByte(), escapeRowList[22]) // pixel5 almost all the blue
        assertEquals(255.toByte(), escapeRowList[3]) // every 4th byte is alpha, should be 255 - test pixel1
        assertEquals(255.toByte(), escapeRowList[7]) // pixel2 alpha byte
    }

    @Test
    fun `should calculate two evenly distributed rows of escape values`() {
        // given
        val fp = FractalPlane(pixelWidth = 22, MAX_I = 128)
        val m = Mandlebrot(fp.MAX_I)
        val row = 0
        val parallelRows = 2

        // when
        val escapeRowList = m.parallelEscapeColourBytesRows(fp, row, parallelRows)
        escapeRowList[0].escapeVals.take(20).chunked(4).forEach { println("Colour: $it") }

        // then
        assertEquals(2, escapeRowList.size)
        assertEquals(0, escapeRowList[0].row)
        assertEquals(8, escapeRowList[1].row) // 22 pixels wide * .75 = 16 pixels high, therefore row 0 & 8
        assertEquals(127.toByte(), escapeRowList[0].escapeVals[0])
        assertEquals(132.toByte(), escapeRowList[0].escapeVals[20])
    }

    @Test
    fun `should calculate 8 evenly distributed rows of escape values`() {
        // given
        val fp = FractalPlane(pixelWidth = 22, MAX_I = 128)
        val m = Mandlebrot(fp.MAX_I)
        val row = 0
        val parallelRows = 8

        // when
        val escapeRowList = m.parallelEscapeColourBytesRows(fp, row, parallelRows)

        escapeRowList[0].escapeVals.take(16).chunked(4).forEach { println("Colour: $it") }
        escapeRowList[1].escapeVals.take(16).chunked(4).forEach { println("Colour: $it") }
        escapeRowList[2].escapeVals.take(16).chunked(4).forEach { println("Colour: $it") }

        // then
        assertEquals(8, escapeRowList.size)
        assertEquals(0, escapeRowList[0].row)
        assertEquals(88, escapeRowList[0].escapeVals.size) // 4 bytes per pixel = 22 * 4
        assertEquals(255.toByte(), escapeRowList[0].escapeVals[3]) // every 4th byte should be 255
        assertEquals(255.toByte(), escapeRowList[0].escapeVals[7])
        assertEquals(2, escapeRowList[1].row) // pixel height should be 16, therefore even rows
        assertEquals(88, escapeRowList[1].escapeVals.size)
        assertEquals(255.toByte(), escapeRowList[1].escapeVals[3])
        assertEquals(255.toByte(), escapeRowList[1].escapeVals[7])
        assertEquals(4, escapeRowList[2].row)
        assertEquals(6, escapeRowList[3].row)
        assertEquals(14, escapeRowList[7].row)
        assertEquals(127.toByte(), escapeRowList[0].escapeVals[0])
        assertEquals(132.toByte(), escapeRowList[0].escapeVals[20])
    }
}
