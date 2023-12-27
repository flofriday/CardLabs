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

    /**
     * Example from Wikipedia
     * https://en.wikipedia.org/wiki/Scheme_(programming_language)#Block_structure
     */
    @Test
    fun hofstadterMaleFemaleSequence() {
        val program =
            """
            (define (hofstadter-male-female n)
              (letrec ((female (lambda (n)
                                 (if (= n 0)
                                   1
                                   (- n (male (female (- n 1)))))))
                        (male (lambda (n)
                                (if (= n 0)
                                  0
                                  (- n (female (male (- n 1))))))))
                (let loop ((i 0))
                  (if (> i n)
                    '()
                    (cons (cons (female i)
                            (male i))
                      (loop (+ i 1)))))))

            (equal?
              (hofstadter-male-female 8)
              '((1 0) (1 0) (2 1) (2 2) (3 2) (3 3) (4 4) (5 4) (5 5)))
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    /**
     * Example from the Scheme Cookbook:
     * https://cookbook.scheme.org/find-depth-of-list/
     */
    @Test
    fun depthOfNestedList() {
        val program =
            """
            (define (depth a)
              (if (pair? a)
                (+ 1 (apply max (map depth a)))
                0))

            (depth '(a (b (c d (e)))))
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(IntegerValue(4), (result as IntegerValue))
    }

    /**
     * Example from the Scheme Cookbook:
     * https://cookbook.scheme.org/create-k-combinations-from-list/
     */
    @Test
    fun combinationOfList() {
        val program =
            """
            (define (combine3 n set rest)
              (letrec
                ((tails-of
                   (lambda (set)
                     (cond ((null? set)
                             '())
                       (else
                         (cons set (tails-of (cdr set)))))))
                  (combinations
                    (lambda (n set)
                      (cond
                        ((zero? n)
                          '())
                        ((= 1 n)
                          (map list set))
                        (else
                          (apply append
                            (map (lambda (tail)
                                   (map (lambda (sub)
                                          (cons (car tail) sub))
                                     (combinations (- n 1) (rest tail))))
                              (tails-of set))))))))
                (combinations n set)))

            ;; create k-combination without repetion
            (define (combine n set)
              (combine3 n set cdr))

            ;; create k-combination with repetition
            (define (combine* n set)
              (combine3 n set (lambda (x) x)))
            """.trimIndent()

        val interpreter = SchemeInterpreter()
        interpreter.run(program)

        var result =
            interpreter.run(
                """
                (equal? 
                    (combine 2 '(a b c))
                    '((a b) (a c) (b c)))
                """.trimIndent(),
            )
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)

        result =
            interpreter.run(
                """
                (equal? 
                    (combine* 2 '(a b c))
                    '((a a) (a b) (a c) (b b) (b c) (c c)))
                """.trimIndent(),
            )
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }
}
