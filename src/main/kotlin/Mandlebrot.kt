class Mandlebrot(val maxIterations: Int) {

    tailrec fun iterationsToEscapeVals(xInit: Float, yInit: Float, iteration: Int = 0, x: Float = 0f, y: Float = 0f) :Int {
        val x1 = x * x - y * y + xInit
        val y1 = 2 * x * y + yInit
        return if (x1*x1 + y1*y1 > 4 || iteration >= maxIterations) iteration
        else iterationsToEscapeVals(xInit, yInit, iteration + 1, x1, y1)
    }

    fun rowEscapeValues(xStart: Float, xEnd: Float, y: Float, length: Int): List<Int> {
        val stepVal = (xEnd - xStart) / length
        return generateSequence(xStart) {
            it + stepVal
        }.map {
            iterationsToEscapeVals(it, y)
        }.take(length).toList()
    }

    // fun rowParallel( ... )
    // step size = height / parallel
    // generateSequence(0) { it + stepSize }
    // .map { rowEscapeValues xstart, xend, it, rowLength }
    // .take(parallel)

}
