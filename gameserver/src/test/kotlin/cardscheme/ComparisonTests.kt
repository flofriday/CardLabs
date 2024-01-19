package cardscheme

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
    fun equalWithFloatAndInt() {
        val program = "(= 0 0.0)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
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
    fun smallerEqualWithFloatAndInt() {
        val program = "(<= 0 0.0)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
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

    @Test
    fun greaterEqualWithFloatAndInt() {
        val program = "(>= 0 0.0)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun isZeroOnZeroInt() {
        val program = "(zero? 0)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun isZeroOnZeroFloat() {
        val program = "(zero? 0.0)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun isZeroOnTwo() {
        val program = "(zero? 2)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun isPositiveOnPositive() {
        val program = "(positive? 45.7)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun isPositiveOnZero() {
        val program = "(positive? 0)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun isPositiveOnNegative() {
        val program = "(positive? -67)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun isNegativeOnPositive() {
        val program = "(negative? 45.7)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun isNegativeOnZero() {
        val program = "(negative? 0)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun isEvenOnEven() {
        val program = "(even? 2)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun isEvenOnOdd() {
        val program = "(even? 1)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun isOddOnEven() {
        val program = "(odd? 2)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun isOddOnOdd() {
        val program = "(odd? 1)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun isEqualOnSameSymbols() {
        val program = "(equal? 'a 'a)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun isEqualOnDifferentSymbols() {
        val program = "(equal? 'a 'b)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun isEqualOnSameFlatList() {
        val program = "(equal? '(a) '(a))"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun isEqualOnDifferentFlatList() {
        val program = "(equal? '(a) '(b))"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun isEqualOnSameNestedList() {
        val program = "(equal? '(a (b) c) '(a (b) c))"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun isEqualOnDifferentNestedList() {
        val program = "(equal? '(a (b) c) '(a (x) c))"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun isEqualOnSameStrings() {
        val program = """(equal? "abc" "abc")"""
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun isEqualOnDifferentStrings() {
        val program = """(equal? "abc" "abxc")"""
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun isEqualOnSameVector() {
        val program = """(equal? (make-vector 5 'a) (make-vector 5 'a))"""
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun isEqualOnDifferentVector() {
        val program = """(equal? (make-vector 5 'a) (make-vector 3 'a))"""
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }
}
