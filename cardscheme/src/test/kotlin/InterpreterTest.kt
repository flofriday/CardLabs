import org.junit.Test
import kotlin.test.assertEquals

class InterpreterTest {

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
}
