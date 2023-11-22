
import org.junit.Assert
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
        val program = """
            (define f (lambda (a b) (+ a b)))
            (f 1 2)
        """.trimMargin()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(3, (result as IntegerValue).value)
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
}
