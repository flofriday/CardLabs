import org.junit.Assert
import org.junit.Test

class LogicTests {
    @Test
    fun andTestWithEqualsAndGreater() {
        val program = "(and (= 2 2) (> 2 1))"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun andTestWithEqualsAndSmaller() {
        val program = "(and (= 2 2) (< 2 1))"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun andTestWithIntegersAndLists() {
        val program = "(and 1 2 '(1 2 3 4 5) '(1 2 3))"
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        Assert.assertEquals(listOf(IntegerValue(1), IntegerValue(2), IntegerValue(3)), (result as ListValue).values.toList())
    }

    @Test
    fun andEmpty() {
        val program = "(and)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun orTestWithEqualsAndGreater() {
        val program = "(or (= 2 2) (> 2 1))"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun orTestWithEqualsAndSmaller() {
        val program = "(or (= 2 2) (< 2 1))"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun orTestWithAllFalse() {
        val program = "(or #f #f #f)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun orTestWithReturnValue() {
        val program = "(or #f #f (+ 2 5))"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(7, (result as IntegerValue).value)
    }

    @Test
    fun notTestWithTrue() {
        val program = "(not #t)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun notTestWithFalse() {
        val program = "(not #f)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun notTestWith3() {
        val program = "(not 3)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun notTestWithList3() {
        val program = "(not (list 3))"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun notTestWithEmptyList() {
        val program = "(not '())"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }
}
