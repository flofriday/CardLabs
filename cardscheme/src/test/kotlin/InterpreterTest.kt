import org.junit.Test
import kotlin.test.assertEquals

class InterpreterTest {

    @Test
    fun simpleTrueBoolean() {
        var program = "#t"
        var result = SchemeInterpreter().run(program)
        assert(result is BoolValue)
        assertEquals(true, (result as BoolValue).value)

        program = "#T"
        result = SchemeInterpreter().run(program)
        assert(result is BoolValue)
        assertEquals(true, (result as BoolValue).value)
    }

    @Test
    fun simpleFalseBoolean() {
        var program = "#f"
        var result = SchemeInterpreter().run(program)
        assert(result is BoolValue)
        assertEquals(false, (result as BoolValue).value)

        program = "#F"
        result = SchemeInterpreter().run(program)
        assert(result is BoolValue)
        assertEquals(false, (result as BoolValue).value)
    }

    @Test
    fun simpleAddition() {
        val program = "(+ 2 3)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntValue)
        assertEquals(5, (result as IntValue).value)
    }

    @Test
    fun simpleSubtraction() {
        val program = "(- 2 3)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntValue)
        assertEquals(-1, (result as IntValue).value)
    }

    @Test
    fun simpleQuoteList() {
        val program = "(quote (1 2 3))"
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(listOf(IntValue(1), IntValue(2), IntValue(3)), (result as ListValue).values)
    }

    @Test
    fun simpleSingleQuoteList() {
        val program = "'(1 2 3)"
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(listOf(IntValue(1), IntValue(2), IntValue(3)), (result as ListValue).values)
    }

    @Test
    fun simpleDefine() {
        val program = "(define flo 42)\nflo"
        val result = SchemeInterpreter().run(program)
        assert(result is IntValue)
        assertEquals(42, (result as IntValue).value)
    }

    @Test
    fun simpleLambda() {
        val program = "((lambda a (+ a a)) 4)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntValue)
        assertEquals(8, (result as IntValue).value)
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
        assert(result is IntValue)
        assertEquals(42, (result as IntValue).value)
    }

    @Test
    fun simpleConditionWithOneExpression() {
        val program = "(if #T (+ 1 2))"
        val result = SchemeInterpreter().run(program)
        assert(result is IntValue)
        assertEquals(3, (result as IntValue).value)
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
        assert(result is IntValue)
        assertEquals(5, (result as IntValue).value)
    }

    @Test
    fun simpleConditionWithTwoExpressionFalse() {
        val program = "(if #f (+ 3 2) (+ 5 8))"
        val result = SchemeInterpreter().run(program)
        assert(result is IntValue)
        assertEquals(13, (result as IntValue).value)
    }

}
