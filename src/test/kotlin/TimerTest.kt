import kotlin.test.Test
import kotlin.test.assertEquals

class TimerTest {

    @Test
    fun shouldGet30FramesPerSecondTarget() {
        val t = Timer()

        // then
        assertEquals(30, t.fpsTarget)
    }


}