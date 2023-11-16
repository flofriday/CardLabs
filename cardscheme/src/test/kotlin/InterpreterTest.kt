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

}
