import org.junit.Assert.assertEquals
import org.junit.Test

class InterpreterTest {

    @Test
    fun simpleTrueBoolean() {
        var program = "#t"
        var result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        assertEquals(true, (result as BooleanValue).value)

        program = "#T"
        result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun simpleFalseBoolean() {
        var program = "#f"
        var result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        assertEquals(false, (result as BooleanValue).value)

        program = "#F"
        result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun simpleAddition() {
        val program = "(+ 2 3)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        assertEquals(5, (result as IntegerValue).value)
    }

    @Test
    fun simpleSubtraction() {
        val program = "(- 2 3)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        assertEquals(-1, (result as IntegerValue).value)
    }

    @Test
    fun simpleQuoteList() {
        val program = "(quote (1 2 3))"
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(listOf(IntegerValue(1), IntegerValue(2), IntegerValue(3)), (result as ListValue).values)
    }

    @Test
    fun simpleSingleQuoteList() {
        val program = "'(1 2 3)"
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(listOf(IntegerValue(1), IntegerValue(2), IntegerValue(3)), (result as ListValue).values)
    }

    @Test
    fun simpleDefine() {
        val program = "(define flo 42)\nflo"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        assertEquals(42, (result as IntegerValue).value)
    }

    @Test
    fun simpleLambda() {
        val program = "((lambda a (+ a a)) 4)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        assertEquals(8, (result as IntegerValue).value)
    }

    @Test
    fun simpleNestedLambda() {
        val program = """
            (define f 
                (lambda a
                    (lambda x (+ a x))))
                    
            ((f 1) 41)
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        assertEquals(42, (result as IntegerValue).value)
    }

    @Test
    fun simpleConditionWithOneExpression() {
        val program = "(if #T (+ 1 2))"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        assertEquals(3, (result as IntegerValue).value)
    }

    @Test
    fun simpleConditionWithOneExpressionFalse() {
        val program = "(if #F (+ 1 2))"
        val result = SchemeInterpreter().run(program)
        assert(result is VoidValue)
    }

    @Test
    fun simpleConditionWithTwoExpressionTrue() {
        val program = "(if #t (+ 3 2) (+ 5 8))"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        assertEquals(5, (result as IntegerValue).value)
    }

    @Test
    fun simpleConditionWithTwoExpressionFalse() {
        val program = "(if #f (+ 3 2) (+ 5 8))"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        assertEquals(13, (result as IntegerValue).value)
    }

}
