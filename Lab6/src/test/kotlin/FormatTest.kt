import formatting.FormatObfuscater
import org.junit.jupiter.api.Test
import java.io.File


class FormatTest {

    private val content = File(filePath).readText()

    private val formatObfuscater = FormatObfuscater()

    @Test
    fun `VISUAL obfuscate submit`() {
        println(content)

        println(formatObfuscater.obfuscate(content))
    }

    companion object {
        private const val filePath = "/home/hornedheck/RiderProjects/bump/Bump/Services/FileManager.cs"
    }
}