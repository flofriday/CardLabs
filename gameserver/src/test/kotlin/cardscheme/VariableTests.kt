package cardscheme

import org.junit.Assert
import org.junit.Test

class VariableTests {
    @Test
    fun simpleDefine() {
        val program =
            """
            (define flo 42)
            flo
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(42, (result as IntegerValue).value)
    }

    @Test
    fun simpleDefineFunction() {
        val program =
            """
            (define (addone n) (+ n 1))
            (addone 41)
            """.trimMargin()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(42, (result as IntegerValue).value)
    }

    @Test
    fun simpleReDefine() {
        val program =
            """
            (define n 42)
            (define n 13)
            n
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(13, (result as IntegerValue).value)
    }

    @Test
    fun simpleDefineUpdate() {
        val program =
            """
            (define n 42)
            (set! n 13)
            n
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(13, (result as IntegerValue).value)
    }

    @Test
    fun defineNested() {
        val program =
            """
            ; At the end g should not be updated
            (define g 2)
            
            (define (badUpdateGlobal n) 
                (define g n)
                n)
               
            (badUpdateGlobal 3)
            g
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(2, (result as IntegerValue).value)
    }

    @Test
    fun defineUpdateNested() {
        val program =
            """
            ; At the end g should be updated
            (define g 2)
            
            (define (correctUpdateGlobal n) 
                (set! g n)
                n)
               
            (correctUpdateGlobal 3)
            g
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(3, (result as IntegerValue).value)
    }

    @Test
    fun simpleLet() {
        val program =
            """
            (let ((x 2) (y 3))
                (* x y))
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(6, (result as IntegerValue).value)
    }

    @Test
    fun simpleLetDoesntLeakVariable() {
        val program =
            """
            (let ((x 2) (y 3))
                (* x y))
            x
            """.trimIndent()
        Assert.assertThrows(SchemeError::class.java) { SchemeInterpreter().run(program) }
    }

    @Test
    fun simpleLetNoSequentialBinding() {
        val program =
            """
            (let ((x 2) (y (+ x 1)))
                (* x y))
            """.trimIndent()
        Assert.assertThrows(SchemeError::class.java) { SchemeInterpreter().run(program) }
    }

    @Test
    fun simpleLetUsesOuterScope() {
        val program =
            """
            (define a 20)
            (define b 1)
            (let ((a (* 2 b)) (b (* 2 a)))
                (+ a b))
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(42, (result as IntegerValue).value)
    }

    @Test
    fun simpleLetStarSequentualBinding() {
        val program =
            """
            (let* ((x 2) (y (+ x 1)))
                (+ x y))
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(5, (result as IntegerValue).value)
    }

    /*
        Adapted from: https://en.wikipedia.org/wiki/Scheme_(programming_language)#Block_structure
     */
    @Test
    fun letStarSequentualBindingFromWikipedia() {
        val program =
            """
            (let* ((var1 10)
                    (var2 (+ var1 12)))
              var2
              )
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(22, (result as IntegerValue).value)
    }

    @Test
    fun simpleLetStarNestedInLet() {
        val program =
            """
            ; This example is from the spec
            (let ((x 2) (y 3))
                (let* ((x 7)
                       (z (+x y)))
                  (* z x)))
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(70, (result as IntegerValue).value)
    }

    @Test
    fun simpleLetRecFunctions() {
        val program =
            """
            ; This example is from the spec
            (define (zero? n) (= n 0))
            
            (letrec ((even?
                    (lambda (n)
                        (if (zero? n)
                            #t
                            (odd? (- n 1)))))
                (odd?
                    (lambda (n)
                        (if (zero? n)
                            #f
                            (even? (- n 1))))))
            (even? 88))
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun simpleLetRecStarFunctions() {
        val program =
            """
            ; This example is from the spec
            (define (zero? n) (= n 0))
            
            (letrec* ((p
                    (lambda (x)
                            (+ 1 (q (- x 1)))))
                (q
                    (lambda (y)
                        (if (zero? y)
                            0
                            (+ 1 (p (- y 1))))))
                (x (p 5))
                (y x))
                y)
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(5, (result as IntegerValue).value)
    }

    @Test
    fun badLetNameClash() {
        val program =
            """
            (let ((a 1) (a 2)) a)
            """.trimIndent()
        Assert.assertThrows(SchemeError::class.java) { SchemeInterpreter().run(program) }
    }

    @Test
    fun letStarNameReuse() {
        val program =
            """
            (let* ((a 1) (a (+ a a))) a)
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(2, (result as IntegerValue).value)
    }

    @Test
    fun badLetRecNameClash() {
        val program =
            """
            (letrec ((a 1) (a 2)) a)
            """.trimIndent()
        Assert.assertThrows(SchemeError::class.java) { SchemeInterpreter().run(program) }
    }

    @Test
    fun badLetRecStarNameClash() {
        val program =
            """
            (letrec* ((a 1) (a 2)) a)
            """.trimIndent()
        Assert.assertThrows(SchemeError::class.java) { SchemeInterpreter().run(program) }
    }

    @Test
    fun variablePrefixKeyword() {
        // There was a bug in the lexer where a prefix in a variable created wrong tokens.
        val program =
            """
            (define dog 1)            ; do keyword
            (define beginning 0)      ;begin keyword
            (define beforedoafter 4)  ;do keyword
            (define prefixdo 4)       ;do keyword
            """.trimIndent()
        SchemeInterpreter().run(program)
    }
}
