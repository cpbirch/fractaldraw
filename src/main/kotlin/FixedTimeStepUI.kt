import org.lwjgl.glfw.GLFWErrorCallback

class FixedTimeStepUI (
    errorCallback: GLFWErrorCallback,
    running: Boolean = false,
    window: Window,
    timer: Timer,
    renderer: Renderer,

) {

    val TARGET_FPS = 75
    val TARGET_UPS = 30

}