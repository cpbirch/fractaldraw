import io.mockk.every
import io.mockk.mockk
import org.lwjgl.glfw.GLFW.glfwGetCursorPos
import kotlin.test.Test
import kotlin.test.assertEquals

class WindowTest {

    @Test
    fun `should get zoom in box dragged left to right`() {
        // given
        val window = mockk<Window>()
        every { window.getMouseXY(any()) } returns (10.0 to 10.0)
        every { window.mouseDownX } returns 0.0
        every { window.mouseDownY } returns 0.0
        every { window.getZoomInBox() } answers { callOriginal() }

        // when
        val box = window.getZoomInBox()

        // then
        assertEquals(0.0, box.get(0))
        assertEquals(0.0, box.get(1))
        assertEquals(10.0, box.get(2))
        assertEquals(10.0, box.get(3))
    }

    @Test
    fun `should get zoom in box dragged right to left`() {
        // given
        val window = mockk<Window>()
        every { window.getMouseXY(any()) } returns (0.0 to 10.0)
        every { window.mouseDownX } returns 10.0
        every { window.mouseDownY } returns 10.0
        every { window.getZoomInBox() } answers { callOriginal() }

        // when
        val box = window.getZoomInBox()

        // then
        assertEquals(0.0, box.get(0))
        assertEquals(10.0, box.get(1))
        assertEquals(10.0, box.get(2))
        assertEquals(10.0, box.get(3))
    }

    @Test
    fun `should get zoom in box dragged bottom to top`() {
        // given
        val window = mockk<Window>()
        every { window.getMouseXY(any()) } returns (10.0 to 0.0)
        every { window.mouseDownX } returns 10.0
        every { window.mouseDownY } returns 10.0
        every { window.getZoomInBox() } answers { callOriginal() }

        // when
        val box = window.getZoomInBox()

        // then
        assertEquals(10.0, box.get(0))
        assertEquals(0.0, box.get(1))
        assertEquals(10.0, box.get(2))
        assertEquals(10.0, box.get(3))
    }
}