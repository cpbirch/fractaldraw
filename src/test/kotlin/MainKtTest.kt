import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MainKtTest {

    @Test
    fun `should parse complex args to a map`() {
        // given
        val args = arrayOf("-a", "-b", "c", "-d", "e", "f", "g", "-h", "--ignore", "--join", "k", "--link", "m", "n", "o", "-p", "q r s")

        // when
        val mapped = Arguments(args)

        // then
        val expected = mapOf(
            "-a" to emptyList(),
            "-b" to listOf("c"),
            "-d" to listOf("e", "f", "g"),
            "-h" to emptyList(),
            "--ignore" to emptyList(),
            "--join" to listOf("k"),
            "--link" to listOf("m", "n", "o"),
            "-p" to listOf("q r s"))

        assertEquals(expected, mapped.argMap)
    }
}