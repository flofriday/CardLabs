package cardscheme

import cardscheme.IntegerValue
import cardscheme.SchemeInterpreter
import cardscheme.VoidValue
import org.junit.Assert
import org.junit.Test

class ConditionalTests {
    @Test
    fun simpleConditionWithOneExpression() {
        val program = "(if #T (+ 1 2))"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(3, (result as IntegerValue).value)
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
        assert(result is IntegerValue)
        Assert.assertEquals(5, (result as IntegerValue).value)
    }

    @Test
    fun simpleConditionWithTwoExpressionFalse() {
        val program = "(if #f (+ 3 2) (+ 5 8))"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(13, (result as IntegerValue).value)
    }

    @Test
    fun simpleConditionWithNumberAsCondition() {
        val program = "(if 42 1 2)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(1, (result as IntegerValue).value)
    }

    @Test
    fun simpleConditionWithZeroAsCondition() {
        val program = "(if 0 1 2)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(1, (result as IntegerValue).value)
    }

    @Test
    fun simpleConditionWithEmptyListAsCondition() {
        val program = "(if '() 1 2)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(1, (result as IntegerValue).value)
    }
}
