package cardscheme

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class AdvancedTests {
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
            (define fib (lambda (n)
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

    @Test
    fun bottlesOfBeerTest() {
        val interpreter = SchemeInterpreter()

        val program =
            """
            (define (bottles-of-beer n)
                (if (= n 0)
                    "No more bottles of beer on the wall, no more bottles of beer.\n"
                    (string-append
                        (string-append
                            (number->string n)
                            " bottle" (if (= n 1) "" "s") " of beer on the wall, ")
                    (string-append
                        (number->string n)
                        " bottle" (if (= n 1) "" "s") " of beer.\n")
                    "Take one down and pass it around, "
                    (string-append
                        (number->string (- n 1))
                        " bottle" (if (= (- n 1) 1) "" "s") " of beer on the wall.\n\n"
                        (bottles-of-beer (- n 1))))))
            """.trimIndent()
        interpreter.run(program)

        val result = interpreter.run("(display (bottles-of-beer 5))")
        Assert.assertEquals(
            """5 bottles of beer on the wall, 5 bottles of beer.
Take one down and pass it around, 4 bottles of beer on the wall.

4 bottles of beer on the wall, 4 bottles of beer.
Take one down and pass it around, 3 bottles of beer on the wall.

3 bottles of beer on the wall, 3 bottles of beer.
Take one down and pass it around, 2 bottles of beer on the wall.

2 bottles of beer on the wall, 2 bottles of beer.
Take one down and pass it around, 1 bottle of beer on the wall.

1 bottle of beer on the wall, 1 bottle of beer.
Take one down and pass it around, 0 bottles of beer on the wall.

No more bottles of beer on the wall, no more bottles of beer.""",
            outputStreamCapture.toString().trim(),
        )
    }

    /**
     * Example from Wikipedia:
     * https://en.wikipedia.org/wiki/Scheme_(programming_language)#Redefinition_of_standard_procedures
     */
    @Test
    fun extendPlusToConcatStrings() {
        val program =
            """
            (set! +
                  (let ((original+ +))
                    (lambda args
                      (apply (if (or (null? args) (string? (car args)))
                                 string-append
                                 original+)
                             args))))
            """.trimIndent()
        val interpreter = SchemeInterpreter()
        interpreter.run(program)

        var result = interpreter.run("""(+ 1 2 3)""")
        assert(result is IntegerValue)
        Assert.assertEquals(6, (result as IntegerValue).value)

        result = interpreter.run("""(+ "1" "2" "3")""")
        assert(result is StringValue)
        Assert.assertEquals("123", (result as StringValue).value)
    }

    /**
     * Example from Wikipedia:
     * https://en.wikipedia.org/wiki/Scheme_(programming_language)#Proper_tail_recursion
     */
    @Test
    fun listOfSquares() {
        val program =
            """
            (define (list-of-squares n)
                (let loop ((i n) (res '()))
                    (if (< i 0)
                        res
                        (loop (- i 1) (cons (* i i) res)))))

            (list-of-squares 9)
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        Assert.assertEquals(ListValue((0..9).map { i -> IntegerValue(i * i) }), (result as ListValue))
    }
}
