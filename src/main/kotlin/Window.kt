import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.glfw.GLFWMouseButtonCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GLCapabilities
import org.lwjgl.system.MemoryUtil.NULL


class Window(
    width: Int = 320,
    height: Int = 240,
    title: CharSequence = "Mandlebrot",
    private var id: Long = -1,
    private var keyCallback: GLFWKeyCallback? = null,
    private var mouseButtonCallback: GLFWMouseButtonCallback? = null,
    var mouseDown: Boolean = false,
    var mouseDownX: Double = 0.0,
    var mouseDownY: Double = 0.0,
    var zoom: Boolean = false,
    var vsync: Boolean = true
) {

    init {
        val caps: GLCapabilities
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwCreateWindow(1, 1, "", NULL, NULL).also {
            glfwMakeContextCurrent(it)
            GL.createCapabilities()
            caps = GL.getCapabilities();
            glfwDestroyWindow(it)
        }

        /* Reset and set window hints */
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)

        /* Create window with specified OpenGL context */
        glfwCreateWindow(width, height, title, NULL, NULL).also {
            id = it
            glfwGetVideoMode(glfwGetPrimaryMonitor())?.also {
                glfwSetWindowPos(
                    id,
                    (it.width() - width) / 2,
                    (it.height() - height) / 2
                )
            }

            /* Create OpenGL context */
            glfwMakeContextCurrent(id)
            GL.createCapabilities()

            /* Enable v-sync */
            if (vsync) {
                glfwSwapInterval(1);
            }

            /* Set key callback */
//            keyCallback = object : GLFWKeyCallback() {
//                override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
//                    if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
//                        glfwSetWindowShouldClose(window, true);
//                    }
//                }
//            }
            keyCallback = glfwSetKeyCallback(id, keyCallback());

            /* Set mouse button callback */
            mouseButtonCallback = glfwSetMouseButtonCallback(id, glfwMouseButtonCallback())
        }
    }

    fun keyCallback() = object : GLFWKeyCallback() {
        override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                glfwSetWindowShouldClose(window, true);
            }
        }
    }

    fun glfwMouseButtonCallback() = object : GLFWMouseButtonCallback() {
        override fun invoke(window: Long, button: Int, action: Int, mods: Int) {
            if (action == org.lwjgl.glfw.GLFW.GLFW_PRESS && !mouseDown) {
                // if mouse down, get cursor position and save
                mouseDown = true
                val xy = getMouseXY(window)
                mouseDownX = xy.first
                mouseDownY = xy.second
                println("mouseDown x: ${mouseDownX}, y: ${mouseDownY}")
            } else {
                mouseDown = false
                zoom = true
                println("mouseUp")
            }
        }
    }

    fun getMouseXY(window: Long): Pair<Double, Double> {
        val xPos = BufferUtils.createDoubleBuffer(1)
        val yPos = BufferUtils.createDoubleBuffer(1)
        glfwGetCursorPos(window, xPos, yPos)
        return xPos.get() to yPos.get()
    }

    fun getZoomInBox(): DoubleArray {
        val xy = getMouseXY(this.id)
        return arrayOf(
            if (mouseDownX > xy.first) xy.first else mouseDownX,
            if (mouseDownY > xy.second) xy.second else mouseDownY,
            if (xy.first > mouseDownX) xy.first else mouseDownX,
            if (xy.second > mouseDownY) xy.second else mouseDownY)
            .toDoubleArray()
    }

    fun showWindow() = glfwShowWindow(id)

    fun isClosing() = glfwWindowShouldClose(id)

    fun setTitle(title: CharSequence) = glfwSetWindowTitle(id, title)

    fun update() {
        glfwSwapBuffers(id)
        glfwPollEvents()
    }

    fun swapBuffer() = glfwSwapBuffers(id)

    fun pollEvents() = glfwPollEvents()

    fun destroy() {
        glfwDestroyWindow(id)
        keyCallback?.free()
        mouseButtonCallback?.free()
    }

    fun setVSync(vsync: Boolean) {
        this.vsync = vsync
        if (vsync) {
            glfwSwapInterval(1)
        } else {
            glfwSwapInterval(0)
        }
    }

    fun isVSyncEnabled() = vsync
}