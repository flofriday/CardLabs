import org.junit.Assert.assertEquals
import org.junit.Test

class ListTests {
    @Test
    fun simpleQuoteList() {
        val program = "(quote (1 2 3))"
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(listOf(IntegerValue(1), IntegerValue(2), IntegerValue(3)), (result as ListValue).values)
    }

    @Test
    fun simpleSingleQuoteList() {
        val program = "'(1 2 3)"
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(listOf(IntegerValue(1), IntegerValue(2), IntegerValue(3)), (result as ListValue).values)
    }

    @Test
    fun mapTestLambda() {
        val program = "(map (lambda (n) (* n n)) '(1 2 3 4 5))"
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(listOf(IntegerValue(1), IntegerValue(4), IntegerValue(9), IntegerValue(16), IntegerValue(25)), (result as ListValue).values)
    }

    @Test
    fun mapTestAddTwoUnevenLists() {
        val program = "(map + '(1 2 3) '(4 5 6 7))"
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(listOf(IntegerValue(5), IntegerValue(7), IntegerValue(9)), (result as ListValue).values)
    }
}
