package cardscheme

import org.junit.Assert
import org.junit.Assert.assertThrows
import org.junit.Test

class FunctionTests {
    @Test
    fun simpleLambda() {
        val program = "((lambda (a) (+ a a)) 4)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(8, (result as IntegerValue).value)
    }

    @Test
    fun twoArgumentLambda() {
        val program =
            """
            (define f (lambda (a b) (+ a b)))
            (f 1 2)
            """
                .trimMargin()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(3, (result as IntegerValue).value)
    }

    @Test
    fun badArityTooManyArgsLambda() {
        val program =
            """
            (define f (lambda (a b) (+ a b)))
            (f 1 2 3)
            """
                .trimMargin()
        assertThrows(SchemeError::class.java) { SchemeInterpreter().run(program) }
    }

    @Test
    fun badArityTooFewArgsLambda() {
        val program =
            """
            (define f (lambda (a b) (+ a b)))
            (f 1)
            """
                .trimMargin()
        assertThrows(SchemeError::class.java) { SchemeInterpreter().run(program) }
    }

    @Test
    fun badArityNoArgsLambda() {
        val program =
            """
            (define f (lambda (a b) (+ a b)))
            (f)
            """
                .trimMargin()
        assertThrows(SchemeError::class.java) { SchemeInterpreter().run(program) }
    }

    @Test
    fun simpleNestedLambda() {
        val program =
            """
            (define f 
                (lambda (a)
                    (lambda (x) (+ a x))))
                    
            ((f 1) 41)
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(42, (result as IntegerValue).value)
    }

    @Test
    fun localDefinition() {
        val program =
            """
            (define g (lambda (n)
            	(define flo (+ n n))
            	flo))
            (g 32)
        """
                .trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(64, (result as IntegerValue).value)
    }

    @Test
    fun multipleLocalDefinition() {
        val program =
            """
            (define g (lambda (n)
            	(define first (+ n n))
            	(define second (+ first 1))
            	second))
            (g 2)
        """
                .trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(5, (result as IntegerValue).value)
    }

    @Test
    fun badLocalDefinitionLeaksVariable() {
        val program =
            """
            (define g (lambda (n)
            	(define flo (+ n n))
            	flo))
            (g 32)
            flo
        """
                .trimIndent()

        assertThrows(SchemeError::class.java) { SchemeInterpreter().run(program) }
    }

    @Test
    fun badLambdaDuplicationArguments() {
        val program =
            """
            (lambda (n m n) (+ 1 1))
            """
                .trimIndent()
        assertThrows(SchemeError::class.java) { SchemeInterpreter().run(program) }
    }

    @Test
    fun badDefineDuplicationArguments() {
        val program =
            """
            (define (f n m n) (+ 1 1))
            """
                .trimIndent()
        assertThrows(SchemeError::class.java) { SchemeInterpreter().run(program) }
    }

    @Test
    fun lambdaShorthandMultipleVarargs() {
        val program =
            """
            ((lambda xs xs) 1 2 3)
            """
                .trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is SchemeValue)
        Assert.assertEquals(
            ListValue(intArrayOf(1, 2, 3).map { i -> IntegerValue(i, null) }, null),
            (result as ListValue),
        )
    }

    @Test
    fun lambdaShorthandSingleVararg() {
        val program =
            """
            ((lambda xs xs) 42)
            """
                .trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is SchemeValue)
        Assert.assertEquals(
            ListValue(
                IntegerValue(42, null),
                schemeSecurityMonitor = null,
            ),
            (result as ListValue),
        )
    }

    @Test
    fun lambdaShorthandNoArgument() {
        val program =
            """
            ((lambda xs xs))
            """
                .trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is SchemeValue)
        Assert.assertEquals(ListValue(schemeSecurityMonitor = null), (result as ListValue))
    }

    @Test
    fun lambdaWithMultipleVararg() {
        val program =
            """
            ((lambda (a b . ns) (list a b ns)) 1 2 3 4 5 6)
            """
                .trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is SchemeValue)
        Assert.assertEquals(
            ListValue(
                IntegerValue(1, null),
                IntegerValue(2, null),
                ListValue(
                    IntegerValue(3, null),
                    IntegerValue(4, null),
                    IntegerValue(5, null),
                    IntegerValue(6, null),
                    schemeSecurityMonitor = null,
                ),
                schemeSecurityMonitor = null,
            ),
            (result as ListValue),
        )
    }

    @Test
    fun lambdaWithNoVararg() {
        val program =
            """
            ((lambda (a b . ns) (list a b ns)) 1 2)
            """
                .trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is SchemeValue)
        Assert.assertEquals(
            ListValue(
                IntegerValue(1, null),
                IntegerValue(2, null),
                ListValue(schemeSecurityMonitor = null),
                schemeSecurityMonitor = null,
            ),
            (result as ListValue),
        )
    }

    @Test
    fun lambdaWithTooFewArgs() {
        val program =
            """
            ((lambda (a b . ns) (list a b ns)) 1)
            """
                .trimIndent()
        assertThrows(SchemeError::class.java) { SchemeInterpreter().run(program) }
    }

    @Test
    fun defineWithMultipleVararg() {
        val program =
            """
            (define (f a b . ns) (list a b ns))
            (f 1 2 3 4 5 6)
            """
                .trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is SchemeValue)
        Assert.assertEquals(
            ListValue(
                IntegerValue(1, null),
                IntegerValue(2, null),
                ListValue(
                    IntegerValue(3, null),
                    IntegerValue(4, null),
                    IntegerValue(5, null),
                    IntegerValue(6, null),
                    schemeSecurityMonitor = null,
                ),
                schemeSecurityMonitor = null,
            ),
            (result as ListValue),
        )
    }

    @Test
    fun defineWithNoVararg() {
        val program =
            """
            (define (f a b . ns) (list a b ns))
            (f 1 2)
            """
                .trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is SchemeValue)
        Assert.assertEquals(
            ListValue(
                IntegerValue(1, null),
                IntegerValue(2, null),
                ListValue(schemeSecurityMonitor = null),
                schemeSecurityMonitor = null,
            ),
            (result as ListValue),
        )
    }

    @Test
    fun defineWithTooFewArgs() {
        val program =
            """
            (define (f a b . ns) (list a b ns))
            (f 1)
            """
                .trimIndent()
        assertThrows(SchemeError::class.java) { SchemeInterpreter().run(program) }
    }

    @Test
    fun defineWithJustVarargs() {
        val program =
            """
            (define (f . ns) ns)
            (f 1 2 3)
            """
                .trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is SchemeValue)
        Assert.assertEquals(
            ListValue(
                IntegerValue(1, null),
                IntegerValue(2, null),
                IntegerValue(3, null),
                schemeSecurityMonitor = null,
            ),
            (result as ListValue),
        )
    }

    @Test
    fun tailCallSkipTest() {
        // At some point the tail call optimization skipped some calls in this code.
        val program =
            """
            (define x 0)
            (let loop ((n 1))
              (if (> n 10)
                '()
                (begin
                  (set! x n)
                  (loop (+ n 1)))))

            x 
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(IntegerValue(10, null), result as IntegerValue)
    }

    @Test
    fun tailCallCorrectEnv() {
        // There was a bug at one point where tail call wouldn't activate the correct environment causing `n` not be
        // found in the closure.
        val program =
            """
            (define f (let ((n 42)) (lambda (a) (+ a n))))

            (define main (lambda () (f 1)))
            (main)
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(IntegerValue(43, null), result as IntegerValue)
    }
}
