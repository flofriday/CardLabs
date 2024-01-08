package cardscheme

import junit.framework.TestCase.assertEquals
import org.junit.Test

class VectorTests {
    @Test
    fun simpleVector() {
        val program = "(vector 1 2 3)"
        val result = SchemeInterpreter().run(program)
        assert(result is VectorValue)
        assertEquals(listOf(IntegerValue(1), IntegerValue(2), IntegerValue(3)), (result as VectorValue).values)
    }

    @Test
    fun simplePoundVector() {
        val program = "#(1 2 3)"
        val result = SchemeInterpreter().run(program)
        assert(result is VectorValue)
        assertEquals(listOf(IntegerValue(1), IntegerValue(2), IntegerValue(3)), (result as VectorValue).values)
    }

    @Test
    fun isVectorOnVector() {
        val program = "(vector? #(1 2 3))"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun isVectorOnBool() {
        val program = "(vector? #f)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun makeVectorWithoutFill() {
        val program = "(make-vector 3)"
        val result = SchemeInterpreter().run(program)
        assert(result is VectorValue)
        assertEquals(3, (result as VectorValue).values.size)
    }

    @Test
    fun makeVectorWithFill() {
        val program = "(make-vector 3 42)"
        val result = SchemeInterpreter().run(program)
        assert(result is VectorValue)
        assertEquals(listOf(IntegerValue(42), IntegerValue(42), IntegerValue(42)), (result as VectorValue).values)
    }

    @Test
    fun vectorLenghtOnLongVector() {
        val program = "(vector-length #(#t #f 123 2 3 '(3 5 6)))"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        assertEquals(6, (result as IntegerValue).value)
    }

    @Test
    fun vectorRef() {
        val program = "(vector-ref #(#f 77 45) 1)"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        assertEquals(77, (result as IntegerValue).value)
    }

    @Test
    fun vectorSet() {
        val program =
            """
            (define v #(1 2 3))
            (vector-set! v 2 42)
            (vector-ref v 2)
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        assertEquals(42, (result as IntegerValue).value)
    }
}