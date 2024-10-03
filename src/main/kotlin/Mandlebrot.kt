import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Mandlebrot(val maxIterations: Int) {

    tailrec fun iterationsToEscapeVals(
        xInit: Float,
        yInit: Float,
        iteration: Int = 0,
        x: Float = 0f,
        y: Float = 0f
    ): Int {
        val x1 = x * x - y * y + xInit
        val y1 = 2 * x * y + yInit
        return if (x1 * x1 + y1 * y1 > 4 || iteration >= maxIterations) iteration
        else iterationsToEscapeVals(xInit, yInit, iteration + 1, x1, y1)
    }

    fun rowEscapeValues(xStart: Float, y: Float, stepVal: Float): Sequence<Int> =
        generateSequence(xStart) { it + stepVal }
            .map { x -> iterationsToEscapeVals(x, y) }

    fun rowEscapeColourBytes(fp: FractalPlane, row: Int) =
        rowEscapeValues(fp.bound.left, fp.toFractalY(row), fp.xScale)
            .flatMap { fp.colourPaletteBytes(it) }
            .take(fp.pixelWidth * 4) // 4 bytes per pixel
            .toList()

    fun parallelEscapeColourBytesRows(fp: FractalPlane, row: Int, parallelRows: Int): List<Row> {
        var rows: List<Row>
        runBlocking {
            val channel = Channel<Row>()
            val stepVal: Int = fp.pixelHeight / parallelRows
            (row..<fp.pixelHeight step stepVal).take(parallelRows).forEach {
//                println("foreach $it of ${fp.pixelHeight} step $stepVal")
                if (it < fp.pixelHeight) launch {
                    val rgbaRow = rowEscapeColourBytes(fp, it)
                    channel.send(Row(it, rgbaRow))
                }
            }
            rows = MutableList(parallelRows) {
                channel.receive()
            }
        }
        return rows
    }

}

data class Row(val row: Int, val escapeVals: List<Byte>)
