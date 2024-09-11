import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GLCapabilities
import org.lwjgl.system.MemoryUtil.NULL


class Window (
    private var id: Long,
    private var keyCallback: GLFWKeyCallback,
    var vsync: Boolean,
    width: Int,
    height: Int,
    title: CharSequence
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
        if (caps.OpenGL32) {
            /* Hints for OpenGL 3.2 core profile */
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)
        } else {
            throw RuntimeException("Neither OpenGL 3.2 nor OpenGL 2.1 is "
                    + "supported, you may want to update your graphics driver.");
        }
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

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

    fun isClosing() = glfwWindowShouldClose(id)

    fun setTitle(title: CharSequence) = glfwSetWindowTitle(id, title)

    fun update() {
        glfwSwapBuffers(id)
        glfwPollEvents()
    }

    fun destroy() {
        glfwDestroyWindow(id)
        keyCallback.free()
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