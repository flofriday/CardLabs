package cardscheme

import org.junit.Assert
import org.junit.Assert.assertThrows
import org.junit.Test

class FunctionTests {
    @Test
    fun simpleLambda() {
        val program = "((lambda a (+ a a)) 4)"
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
                (lambda a
                    (lambda x (+ a x))))
                    
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
}
