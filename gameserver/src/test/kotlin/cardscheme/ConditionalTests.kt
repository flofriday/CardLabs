package cardscheme

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

    @Test
    fun conditionalWithCondTest1() {
        val program = """(define n 0)
    (cond ((= n 0) "zero")
    ((= n 1) "one")
    ((= n 2) "two")
    (else    "other"))"""
        val result = SchemeInterpreter().run(program)
        assert(result is StringValue)
        Assert.assertEquals("zero", (result as StringValue).value)
    }

    @Test
    fun conditionalWithCondTest2() {
        val program = """(define n 1)
    (cond ((= n 0) "zero")
    ((= n 1) "one")
    ((= n 2) "two")
    (else    "other"))"""
        val result = SchemeInterpreter().run(program)
        assert(result is StringValue)
        Assert.assertEquals("one", (result as StringValue).value)
    }

    @Test
    fun conditionalWithCondTest3() {
        val program = """(define n 2)
    (cond ((= n 0) "zero")
    ((= n 1) "one")
    ((= n 2) "two")
    (else    "other"))"""
        val result = SchemeInterpreter().run(program)
        assert(result is StringValue)
        Assert.assertEquals("two", (result as StringValue).value)
    }

    @Test
    fun conditionalWithCondTestElse() {
        val program = """(define n 5)
    (cond ((= n 0) "zero")
    ((= n 1) "one")
    ((= n 2) "two")
    (else    "other"))"""
        val result = SchemeInterpreter().run(program)
        assert(result is StringValue)
        Assert.assertEquals("other", (result as StringValue).value)
    }



}
