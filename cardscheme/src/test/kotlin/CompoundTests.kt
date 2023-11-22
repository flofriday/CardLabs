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

    @Test
    fun fibonacciTest() {
        val interpreter = SchemeInterpreter()

        val program =
            """
            (define fib (lambda n
                (if (< n 3)
                    1
                    (+ (fib (- n 1)) (fib (- n 2)))
            )))
            """.trimIndent()
        interpreter.run(program)

        var result = interpreter.run("(fib 1)")
        assert(result is IntegerValue)
        Assert.assertEquals(1, (result as IntegerValue).value)

        result = interpreter.run("(fib 2)")
        assert(result is IntegerValue)
        Assert.assertEquals(1, (result as IntegerValue).value)

        result = interpreter.run("(fib 3)")
        assert(result is IntegerValue)
        Assert.assertEquals(2, (result as IntegerValue).value)

        result = interpreter.run("(fib 4)")
        assert(result is IntegerValue)
        Assert.assertEquals(3, (result as IntegerValue).value)

        result = interpreter.run("(fib 5)")
        assert(result is IntegerValue)
        Assert.assertEquals(5, (result as IntegerValue).value)

        result = interpreter.run("(fib 6)")
        assert(result is IntegerValue)
        Assert.assertEquals(8, (result as IntegerValue).value)

        result = interpreter.run("(fib 7)")
        assert(result is IntegerValue)
        Assert.assertEquals(13, (result as IntegerValue).value)

        result = interpreter.run("(fib 8)")
        assert(result is IntegerValue)
        Assert.assertEquals(21, (result as IntegerValue).value)
    }
}
