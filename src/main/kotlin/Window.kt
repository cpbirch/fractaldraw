import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GLCapabilities
import org.lwjgl.system.MemoryUtil.NULL


class Window (
    width: Int = 320,
    height: Int = 240,
    title: CharSequence = "Mandlebrot",
    private var id: Long = -1,
    private var keyCallback: GLFWKeyCallback? = null,
    var vsync: Boolean = true
) {

    init {
        val caps : GLCapabilities
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
                glfwSetWindowPos(id,
                    (it.width() - width) / 2,
                    (it.height() - height) / 2)
            }

            /* Create OpenGL context */
            glfwMakeContextCurrent(id)
            GL.createCapabilities()

            /* Enable v-sync */
            if (vsync) {
                glfwSwapInterval(1);
            }

            /* Set key callback */
            keyCallback = object: GLFWKeyCallback() {
                override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
                    if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                        glfwSetWindowShouldClose(window, true);
                    }
                }
            }
            glfwSetKeyCallback(id, keyCallback);
        }
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