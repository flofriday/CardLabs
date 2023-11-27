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

    @Test
    fun simpleAbs() {
        val program = "(abs (- 0 9))"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(9, (result as IntegerValue).value)
    }

    @Test
    fun simpleAbsOnPositive() {
        val program = "(abs 3)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(3, (result as IntegerValue).value)
    }

    @Test
    fun simpleSqrt() {
        val program = "(sqrt 9.0)"
        val result = SchemeInterpreter().run(program)
        assert(result is FloatValue)
        Assert.assertEquals(3, (result as FloatValue).value.toInt())
    }

    @Test
    fun simpleModulo() {
        val program = "(modulo 5 2)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(1, (result as IntegerValue).value)
    }

    @Test
    fun simpleModulo2() {
        val program = "(modulo 11 6)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(5, (result as IntegerValue).value)
    }
}
