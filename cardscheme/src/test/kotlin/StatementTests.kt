import org.junit.Assert
import org.junit.Test

class StatementTests {
    @Test
    fun simpleDefine() {
        val program = "(define flo 42)\nflo"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(42, (result as IntegerValue).value)
    }
}
