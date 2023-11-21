import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class CompoundTests {
    private val outputStreamCapture = ByteArrayOutputStream()

    @Before
    fun setUp() {
        System.setOut(PrintStream(outputStreamCapture))
    }

    @After
    fun tearDown() {
        System.setOut(System.out)
    }

    @Test
    fun beginAndDisplayTest() {
        val program =
            """
            (begin
	            (display 1)
	            (display 2)
	            (display (+ 1 2)))
            """.trimIndent()
        SchemeInterpreter().run(program)
        Assert.assertEquals("123", outputStreamCapture.toString().trim())
    }
}
