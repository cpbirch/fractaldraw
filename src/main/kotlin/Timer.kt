import org.lwjgl.glfw.GLFW.glfwGetTime

class Timer(
    var lastLoopTime: Double = glfwGetTime(),
    var timeCount: Float = 0f,
    var fpsTarget: Int = 30,
    var fps: Int = 0,
    var fpsCount: Int = 0,
    var ups: Int = 0,
    var upsCount: Int = 0
) {

    fun init(): Unit {
        lastLoopTime = glfwGetTime()
    }

    fun getTime() = glfwGetTime()

    fun getDelta() = getTime().let {
        val delta = (it - lastLoopTime).toFloat()
        lastLoopTime = it
        timeCount += delta
        delta
    }

    fun updateFPS() = fpsCount++

    fun updateUPS() = upsCount++

    fun update(): Unit {
        if (timeCount > 1f) {
            fps = fpsCount
            fpsCount = 0
            ups = upsCount
            upsCount = 0
            timeCount -= 1f
        }
    }

    fun getFPS() = if (fps > 0) fps else fpsCount

}