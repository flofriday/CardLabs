import org.junit.Assert
import org.junit.Test

class ComparisonTests {
    @Test
    fun smallerThanWithInts1() {
        val program = "(< 1 2)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun smallerThanWithInts2() {
        val program = "(< 2 2)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun smallerThanWithInts3() {
        val program = "(< 8 5)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun smallerThanWithFloats1() {
        val program = "(< 5.1 5.11)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun smallerThanWithFloats2() {
        val program = "(< 100.32 99.999)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun smallerThanWithIntAndFloat1() {
        val program = "(< 100 100.001)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun smallerThanWithIntAndFloat2() {
        val program = "(< 99.99 99)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun smallerThanWithIntAndFloat3() {
        val program = "(< 10.5 10.5)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun smallerThanWithManyInts1() {
        val program = "(< 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun smallerThanWithManyInts2() {
        val program = "(< 10 9 8 7 6 5 4 3 2 1)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun smallerThanWithManyInts3() {
        val program = "(< 1 2 3 4 5 6 7 8 9 10 11 12 13 14 14)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun smallerThanWithManyInts4() {
        val program = "(< 1 2 3 4 5 6 7 8 9 10 11 12 13 5)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun equalWithManyInts1() {
        val program = "(= 5 5 5 5 5 5 5)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun equalWithManyInts2() {
        val program = "(= 1 1)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun equalWithManyInts3() {
        val program = "(= 1 2)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun equalWithManyInts4() {
        val program = "(= 2 1)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun equalWithManyFloats1() {
        val program = "(= 5.1 5.1)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun equalWithManyFloats2() {
        val program = "(= 5.1 5.11)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun smallerEqualWithManyInts1() {
        val program = "(<= 1 2 3 4 5 6 7 8 9 10 11 12 13 14 14)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun smallerEqualWithManyInts2() {
        val program = "(<= 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun smallerEqualWithManyInts3() {
        val program = "(<= 1 2 3 4 5 6 7 10 9 10 11 12 13 14 15)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun greaterWithManyInts1() {
        val program = "(> 5 4 3 2 1 1)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun greaterWithManyInts2() {
        val program = "(> 5 4 3 2 1)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun greaterEqualWithManyInts1() {
        val program = "(>= 5 4 3 2 1 1)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun greaterEqualWithInts1() {
        val program = "(>= 2 2)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }
}
