package cardscheme

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

    @Test
    fun absTest1() {
        val program = "(abs -10)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(10, (result as IntegerValue).value)
    }

    @Test
    fun absTest2() {
        val program = "(abs -44.38)"
        val result = SchemeInterpreter().run(program)
        assert(result is FloatValue)
        Assert.assertEquals(44.38f, (result as FloatValue).value)
    }

    @Test
    fun absTest3() {
        val program = "(abs 15)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(15, (result as IntegerValue).value)
    }

    @Test
    fun absTest4() {
        val program = "(abs 15.3844)"
        val result = SchemeInterpreter().run(program)
        assert(result is FloatValue)
        Assert.assertEquals(15.3844f, (result as FloatValue).value)
    }

    @Test
    fun maxOnInts() {
        val program = "(max 1 2 3)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(3, (result as IntegerValue).value)
    }

    @Test
    fun maxOnSingleInt() {
        val program = "(max 42)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(42, (result as IntegerValue).value)
    }

    @Test
    fun maxOnFloats() {
        val program = "(max 1.0  42.42 31.141516)"
        val result = SchemeInterpreter().run(program)
        assert(result is FloatValue)
        Assert.assertEquals(42.42f, (result as FloatValue).value)
    }

    @Test
    fun minOnInts() {
        val program = "(min 1 2 3)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(1, (result as IntegerValue).value)
    }

    @Test
    fun minOnSingleInt() {
        val program = "(min 42)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(42, (result as IntegerValue).value)
    }

    @Test
    fun minOnFloats() {
        val program = "(min 1.0  42.42 31.141516)"
        val result = SchemeInterpreter().run(program)
        assert(result is FloatValue)
        Assert.assertEquals(1.0f, (result as FloatValue).value)
    }
}
