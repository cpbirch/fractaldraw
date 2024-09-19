import kotlinx.coroutines.CoroutineScope
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

    fun rowEscapeValues(xStart: Float, y: Float, stepVal: Float): Sequence<Int> {
        return generateSequence(xStart) {
            it + stepVal
        }.map {
            iterationsToEscapeVals(it, y)
        }
    }

    fun rowEscapeValuesAsList(xStart: Float, xEnd: Float, y: Float, length: Int): List<Int> {
        val stepVal = (xEnd - xStart) / length
        return rowEscapeValues(xStart, y, stepVal).take(length).toList()
    }

    fun rowEscapeValuesAsList(fp: FractalPlane, row: Int): List<Int> {
        return rowEscapeValuesAsList(
            fp.bound.left,
            fp.bound.right,
            fp.toFractalY(row),
            fp.pixelWidth
        )
    }

    fun rowParallelEscapeValuesAsList(fp: FractalPlane, row: Int, parallelRows: Int): List<Row> {
        var rows: List<Row>
        runBlocking {
            val channel = Channel<Row>()
            val stepVal: Int = fp.pixelHeight / parallelRows
            (row..< fp.pixelHeight step stepVal).forEach {
//                println("row: $it")
                launch {
                    channel.send(
                        Row(
                            it, rowEscapeValuesAsList(
                                fp.bound.left,
                                fp.bound.right,
                                fp.toFractalY(it),
                                fp.pixelWidth
                            )
                        )
                    )
                }
            }
            rows = MutableList<Row>(parallelRows) {
                channel.receive()
            }
        }
        return rows
    }

    // fun rowParallel( ... )
    // step size = height / parallel
    // generateSequence(0) { it + stepSize }
    // .map { rowEscapeValues xstart, xend, it, rowLength }
    // .take(parallel)

}

data class Row(val row: Int, val escapeVals: List<Int>)
