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
        val outputBuffer = StringBuilder()
        SchemeInterpreter(outputBuffer).run(program)
        Assert.assertEquals("", outputStreamCapture.toString().trim())
        Assert.assertEquals("123", outputBuffer.toString())
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
        val outputBuffer = StringBuilder()
        val interpreter = SchemeInterpreter(outputBuffer)

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
            outputBuffer.toString().trim(),
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

    /**
     * Example from the Scheme Cookbook:
     * https://cookbook.scheme.org/find-index-of-element-in-list/
     */
    @Test
    fun indexInList() {
        val program =
            """
            ; You have a list and a procedure and you want to find the first index for an element for
            ; which the procedure returns true.
            (define (list-index fn list)
              (let iter ((list list) (index 0))
                (if (null? list)
                  -1
                  (let ((item (car list)))
                    (if (fn item)
                      index
                      (iter (cdr list) (+ index 1)))))))

            (define g10 (lambda (x) (> x 10)))
            (list-index g10 '(1 2 3 4 10 11 12 13 14))
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(IntegerValue(5), (result as IntegerValue))
    }

    /**
     * Example from the Scheme Cookbook:
     * https://cookbook.scheme.org/join-list-of-strings-with-delimiter/
     */
    @Test
    fun stringJoinWithLoop() {
        val program =
            """
            (define (string-join lst delimiter)
              (if (null? lst) ""
                              (let loop ((result (car lst)) (lst (cdr lst)))
                                (if (null? lst)
                                  result
                                  (loop (string-append result delimiter (car lst))
                                    (cdr lst))))))

            (string-join '("foo" "bar" "baz") ":")
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is StringValue)
        Assert.assertEquals(StringValue("foo:bar:baz"), (result as StringValue))
    }

    /**
     * Example from the Scheme Cookbook:
     * https://cookbook.scheme.org/remove-element-from-list/
     */
    @Test
    fun removeElementsFromList() {
        val program = """
        (define (remove fn lst)
          (let loop ((lst lst) (result '()))
            (if (null? lst)
              (reverse result)
              (let ((item (car lst)))
                (loop (cdr lst)
                  (if (fn item) result (cons item result)))))))

        (define g10 (lambda (x) (> x 10)))

        (remove g10 '(1 2 3 4 10 11 12 13 14))
        """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        Assert.assertEquals(ListValue(listOf(1, 2, 3, 4, 10).map { i -> IntegerValue(i) }), (result as ListValue))
    }

    /**
     * Example from the Scheme Cookbook:
     * https://cookbook.scheme.org/compute-pascals-triangle/
     */
    @Test
    fun pascalsTriangle() {
        val program = """
        (define (map-iota proc limit)
          (let loop ((i 0) (result '()))
            (if (< i limit)
              (loop (+ i 1) (cons (proc i) result))
              (reverse result))))

        (define (pascal n k)
          (cond ((or (= k 0) (= k n))
                  1)
            ((< 0 k n)
              (+ (pascal (- n 1) (- k 1))
                (pascal (- n 1) k)))
            (else
              (error "Bad indexes" n k))))

        (define (pascal-row n)
          (map-iota (lambda (k) (pascal n k))
            (+ n 1)))

        (define (pascal-triangle number-of-rows)
          (map-iota pascal-row number-of-rows))

        (equal?
          (pascal-triangle 10)
          '((1)
            (1 1)
            (1 2 1)
            (1 3 3 1)
            (1 4 6 4 1)
            (1 5 10 10 5 1)
            (1 6 15 20 15 6 1)
            (1 7 21 35 35 21 7 1)
            (1 8 28 56 70 56 28 8 1)
            (1 9 36 84 126 126 84 36 9 1))
        ) 
        """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    /**
     * Example from the Scheme Cookbook:
     * https://cookbook.scheme.org/use-lists-as-2d-matrix/
     */
    @Test
    fun matrixMultiplication() {
        val program = """
        (define (make-matrix n)
          (let outter ((i n) (result '()))
            (if (= i 0)
                result
                (outter (- i 1)
                        (cons
                         (let inner ((j n) (row '()))
                           (if (= j 0)
                               row
                               (inner (- j 1) (cons (if (= i j) 1 0) row))))
                         result)))))

        (define (nth list n)
          (let loop ((n n) (result list))
            (if (= n 0)
                (car result)
                (loop (- n 1)
                      (cdr result)))))

        (define matrix-row nth)

        (define (matrix-col M n)
          (let loop ((i (length M)) (result '()))
            (if (= i 0)
                result
                (loop (- i 1)
                      (cons (nth (nth M (- i 1)) n) result)))))

        (define (matrix-mul N M)
          (let outter ((i (length N)) (result '()))
            (if (= i 0)
                result
                (outter (- i 1)
                        (cons
                         (let inner ((j (length (car M))) (row '()))
                           (if (= j 0)
                               row
                               (inner
                                (- j 1)
                                (cons (reduce + (map *
                                                     (matrix-row N (- i 1))
                                                     (matrix-col M (- j 1))))
                                      row))))
                       result)))))

        (define (reduce fun lst)
          (let loop ((result (car lst)) (lst (cdr lst)))
            (if (null? lst)
                result
                (loop (fun result (car lst)) (cdr lst)))))

        (define (matrix-vector-mul v M)
          (car (matrix-mul (list v) M)))

        (define (matrix-transpose M)
          (let loop ((M M) (result '()))
            (if (null? (car M))
                result
                (loop (map cdr M) (append result (list (map car M)))))))

        (define (matrix-sum N M)
          (map (lambda (nrow mrow) (map + nrow mrow)) N M))        
          
        (define M1 '((1 2 3) (2 3 4) (3 2 1)))
        (define M2 (make-matrix 3))
        """.trimIndent()
        val interpreter = SchemeInterpreter()
        interpreter.run(program)

        var result = interpreter.run("(matrix-mul M1 M2)")
        assert(result is ListValue)
        Assert.assertEquals(
            ListValue(
                ListValue(listOf(1, 2, 3).map { i -> IntegerValue(i) }),
                ListValue(listOf(2, 3, 4).map { i -> IntegerValue(i) }),
                ListValue(listOf(3, 2, 1).map { i -> IntegerValue(i) }),
            ), (result as ListValue)
        )

        result = interpreter.run("(matrix-mul M1 '((2 3 1) (1 2 1) (1 3 1)))")
        assert(result is ListValue)
        Assert.assertEquals(
            ListValue(
                ListValue(listOf(7, 16, 6).map { i -> IntegerValue(i) }),
                ListValue(listOf(11, 24, 9).map { i -> IntegerValue(i) }),
                ListValue(listOf(9, 16, 6).map { i -> IntegerValue(i) }),
            ), (result as ListValue)
        )

        result = interpreter.run("(matrix-sum M1 M2)")
        assert(result is ListValue)
        Assert.assertEquals(
            ListValue(
                ListValue(listOf(2, 2, 3).map { i -> IntegerValue(i) }),
                ListValue(listOf(2, 4, 4).map { i -> IntegerValue(i) }),
                ListValue(listOf(3, 2, 2).map { i -> IntegerValue(i) }),
            ), (result as ListValue)
        )

        result = interpreter.run("(matrix-vector-mul '(2 3 1) M1)")
        assert(result is ListValue)
        Assert.assertEquals(
            ListValue(listOf(11, 15, 19).map { i -> IntegerValue(i) }), (result as ListValue)
        )
    }
}
