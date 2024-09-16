class Mandlebrot(val maxIterations: Int) {
    fun iterationsToEscapeValue(xInit: Float, yInit: Float): Int {
        var x2 = 0f // x2:= 0
        var y2 = 0f // y2:= 0
        var x = 0.0f
        var y = 0.0f
        var iteration = 0

        while ((x2 + y2) <= 4 && (iteration < maxIterations)) { //while (x2 + y2 ≤ 4 and iteration < max_iteration) do
            y = 2 * x * y + yInit //    y:= 2 * x * y + y0
            x = x2 - y2 + xInit //    x:= x2 - y2 + x0
            x2 = x * x //    x2:= x * x
            y2 = y * y //    y2:= y * y
            iteration++ //    iteration:= iteration + 1
        }
        return iteration
    }

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

}
