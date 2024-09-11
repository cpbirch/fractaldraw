import org.lwjgl.opengl.GL11.*
import java.nio.FloatBuffer

class Renderer (var vertices: FloatBuffer,
    var numVertices: Int,
    var drawing: Boolean,
    font: Font = Font(12, false)) {

    fun clear() = glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

    fun begin() {
        check(!drawing) { "Renderer is already drawing!" }
        drawing = true
        numVertices = 0
    }

    fun end() {
        check(drawing) { "Renderer isn't drawing!" }
        drawing = false
        flush()
    }

    fun flush() {
        if (numVertices > 0) {
            vertices.flip()

            /* Draw batch */
            glDrawArrays(GL_TRIANGLES, 0, numVertices)

            /* Clear vertex data for next batch */
            vertices.clear()
            numVertices = 0
        }
    }
}