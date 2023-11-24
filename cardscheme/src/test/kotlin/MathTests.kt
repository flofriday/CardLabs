import org.junit.Assert
import org.junit.Assert.assertThrows
import org.junit.Test

class MathTests {
    @Test
    fun simpleIntAddition() {
        val program = "(+ 2 3)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(5, (result as IntegerValue).value)
    }

    @Test
    fun simpleIntSubtraction() {
        val program = "(- 2 3)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(-1, (result as IntegerValue).value)
    }

    @Test
    fun simpleIntMultiplication() {
        val program = "(* 2 3)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(6, (result as IntegerValue).value)
    }

    @Test
    fun simpleIntDivision() {
        val program = "(/ 6 3)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(2, (result as IntegerValue).value)
    }

    @Test
    fun badIncompatibleTypesAdd() {
        val program = "(+ 3 4 #t)"
        assertThrows(SchemeError::class.java) { SchemeInterpreter().run(program) }
    }
}
