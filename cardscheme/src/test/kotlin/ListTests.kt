import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class ListTests {
    @Test
    fun simpleQuoteList() {
        val program = "(quote (1 2 3))"
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(listOf(IntegerValue(1), IntegerValue(2), IntegerValue(3)), (result as ListValue).values.toList())
    }

    @Test
    fun simpleSingleQuoteList() {
        val program = "'(1 2 3)"
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(listOf(IntegerValue(1), IntegerValue(2), IntegerValue(3)), (result as ListValue).values.toList())
    }

    @Test
    fun mapTestLambda() {
        val program = "(map (lambda (n) (* n n)) '(1 2 3 4 5))"
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(
            listOf(IntegerValue(1), IntegerValue(4), IntegerValue(9), IntegerValue(16), IntegerValue(25)),
            (result as ListValue).values.toList(),
        )
    }

    @Test
    fun mapTestAddTwoUnevenLists() {
        val program = "(map + '(1 2 3) '(4 5 6 7))"
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(listOf(IntegerValue(5), IntegerValue(7), IntegerValue(9)), (result as ListValue).values.toList())
    }

    @Test
    fun badMapTooManyArguments() {
        val program = """
            (define (id n) n)
            (map id '( 12 3 4) '(4 5 6))
        """
        Assert.assertThrows(SchemeError::class.java) { SchemeInterpreter().run(program) }
    }

    @Test
    fun carTest() {
        val program = "(car (list 1 2 3))"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        assertEquals(1, (result as IntegerValue).value)
    }

    @Test
    fun cdrTest() {
        val program = "(cdr (list 1 2 3))"
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(listOf(IntegerValue(2), IntegerValue(3)), (result as ListValue).values.toList())
    }

    @Test
    fun compoundListTest() {
        val interpreter = SchemeInterpreter()
        var result =
            interpreter.run(
                """(define lst (list 1 2))
lst""",
            )
        assert(result is ListValue)
        assertEquals(listOf(IntegerValue(1), IntegerValue(2)), (result as ListValue).values.toList())

        result = interpreter.run("(cons 0 lst)")
        assert(result is ListValue)
        assertEquals(listOf(IntegerValue(0), IntegerValue(1), IntegerValue(2)), (result as ListValue).values.toList())

        result = interpreter.run("lst")
        assert(result is ListValue)
        assertEquals(listOf(IntegerValue(1), IntegerValue(2)), (result as ListValue).values.toList())

        result = interpreter.run("(append lst (list 3 4))")
        assert(result is ListValue)
        assertEquals(listOf(IntegerValue(1), IntegerValue(2), IntegerValue(3), IntegerValue(4)), (result as ListValue).values.toList())

        result = interpreter.run("lst")
        assert(result is ListValue)
        assertEquals(listOf(IntegerValue(1), IntegerValue(2)), (result as ListValue).values.toList())

        result = interpreter.run("(car lst)")
        assert(result is IntegerValue)
        assertEquals(1, (result as IntegerValue).value)

        result = interpreter.run("(cdr lst)")
        assert(result is ListValue)
        assertEquals(listOf(IntegerValue(2)), (result as ListValue).values.toList())

        result = interpreter.run("(cdr (cdr lst))")
        assert(result is ListValue)
        assertEquals(emptyList<SchemeValue>(), (result as ListValue).values.toList())

        result = interpreter.run("(length lst)")
        assert(result is IntegerValue)
        assertEquals(2, (result as IntegerValue).value)
    }
}
