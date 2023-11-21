import org.junit.Assert
import org.junit.Test

class MathTests {
    @Test
    fun simpleAddition() {
        val program = "(+ 2 3)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(5, (result as IntegerValue).value)
    }

    @Test
    fun simpleSubtraction() {
        val program = "(- 2 3)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(-1, (result as IntegerValue).value)
    }
}