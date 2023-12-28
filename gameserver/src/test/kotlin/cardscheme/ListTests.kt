package cardscheme

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class ListTests {
    @Test
    fun simpleQuoteList() {
        val program = "(quote (1 2 3))"
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(listOf(IntegerValue(1, null), IntegerValue(2, null), IntegerValue(3, null)), (result as ListValue).values.toList())
    }

    @Test
    fun simpleSingleQuoteList() {
        val program = "'(1 2 3)"
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(listOf(IntegerValue(1, null), IntegerValue(2, null), IntegerValue(3, null)), (result as ListValue).values.toList())
    }

    @Test
    fun mapTestLambda() {
        val program = "(map (lambda (n) (* n n)) '(1 2 3 4 5))"
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(
            listOf(IntegerValue(1, null), IntegerValue(4, null), IntegerValue(9, null), IntegerValue(16, null), IntegerValue(25, null)),
            (result as ListValue).values.toList(),
        )
    }

    @Test
    fun mapTestAddTwoUnevenLists() {
        val program = "(map + '(1 2 3) '(4 5 6 7))"
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(listOf(IntegerValue(5, null), IntegerValue(7, null), IntegerValue(9, null)), (result as ListValue).values.toList())
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
        assertEquals(listOf(IntegerValue(2, null), IntegerValue(3, null)), (result as ListValue).values.toList())
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
        assertEquals(listOf(IntegerValue(1, null), IntegerValue(2, null)), (result as ListValue).values.toList())

        // Prepend to a copy of the list
        result = interpreter.run("(cons 0 lst)")
        assert(result is ListValue)
        assertEquals(listOf(IntegerValue(0, null), IntegerValue(1, null), IntegerValue(2, null)), (result as ListValue).values.toList())

        // Ensure the list hasn't changed
        result = interpreter.run("lst")
        assert(result is ListValue)
        assertEquals(listOf(IntegerValue(1, null), IntegerValue(2, null)), (result as ListValue).values.toList())

        result = interpreter.run("(append lst (list 3 4))")
        assert(result is ListValue)
        assertEquals(
            listOf(IntegerValue(1, null), IntegerValue(2, null), IntegerValue(3, null), IntegerValue(4, null)),
            (result as ListValue).values.toList(),
        )

        result = interpreter.run("lst")
        assert(result is ListValue)
        assertEquals(listOf(IntegerValue(1, null), IntegerValue(2, null)), (result as ListValue).values.toList())

        result = interpreter.run("(car lst)")
        assert(result is IntegerValue)
        assertEquals(1, (result as IntegerValue).value)

        result = interpreter.run("(cdr lst)")
        assert(result is ListValue)
        assertEquals(listOf(IntegerValue(2, null)), (result as ListValue).values.toList())

        result = interpreter.run("(cdr (cdr lst))")
        assert(result is ListValue)
        assertEquals(emptyList<SchemeValue>(), (result as ListValue).values.toList())

        result = interpreter.run("(length lst)")
        assert(result is IntegerValue)
        assertEquals(2, (result as IntegerValue).value)
    }

    @Test
    fun quotedNestedListTest() {
        val program = "'(1 (2 3))"
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(
            ListValue(
                null,
                IntegerValue(1, null),
                ListValue(null, IntegerValue(2, null), IntegerValue(3, null)),
            ),
            result as ListValue,
        )
    }

    @Test
    fun quotedListOfManyLiterals() {
        val program = """'(1 3.14 #t #f "text" #\c)"""
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(
            ListValue(
                null,
                IntegerValue(1, null),
                FloatValue(3.14.toFloat(), null),
                BooleanValue(true, null),
                BooleanValue(false, null),
                StringValue("text", null),
                CharacterValue('c', null),
            ),
            result as ListValue,
        )
    }

    @Test
    fun quotedListOfSymbol() {
        val program = """'(display 34)"""
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(
            ListValue(
                null,
                SymbolValue("display", null),
                IntegerValue(34, null),
            ),
            result as ListValue,
        )
    }

    @Test
    fun quotedListOfKeywords() {
        val program = """'(begin cond define do else if lambda let let* letrec letrec* set! quote)"""
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(
            ListValue(
                "begin cond define do else if lambda let let* letrec letrec* set! quote".split(" ")
                    .map { kw -> SymbolValue(kw, null) },
                null,
            ),
            result as ListValue,
        )
    }

    @Test
    fun isNullOnEmptyList() {
        val program = "(null? '())"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun isNullOnFullList() {
        val program = "(null? '(1 2 3))"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun isNullOnZero() {
        val program = "(null? 0)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun isPairOnList() {
        val program = "(pair? '(1 2 3))"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun isPairOnEmptyList() {
        val program = "(pair? '())"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun isPairOnInt() {
        val program = "(pair? 20)"
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun applyListToBuiltin() {
        val program = "(apply + '(1 2 3 4))"
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        assertEquals(10, (result as IntegerValue).value)
    }

    @Test
    fun applyListToUserFunction() {
        val program =
            """
            (define (f a b) (+ a b))
            (apply f '(2 8))
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        assertEquals(10, (result as IntegerValue).value)
    }

    @Test
    fun applyListToUserVarargFunction() {
        val program =
            """
            (define (f . ns) (car ns))
            (apply f '(2 8))
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        assertEquals(2, (result as IntegerValue).value)
    }

    @Test
    fun badApplyListToFunctionWithTooFewArguments() {
        val program =
            """
            (define (f a b c d) (car ns))
            (apply f '(2 8 5))
            """.trimIndent()
        Assert.assertThrows(SchemeError::class.java) { SchemeInterpreter().run(program) }
    }

    @Test
    fun setCarOfList() {
        val program =
            """
            (define l '(1 2 3))
            (set-car! l 'a)
            l
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(
            ListValue(
                null,
                SymbolValue("a", null),
                IntegerValue(2, null),
                IntegerValue(3, null),
            ),
            (result as ListValue),
        )
    }

    @Test
    fun setCdrOfListToSymbol() {
        val program =
            """
            (define l '(1 2 3))
            (set-cdr! l 'a)
            l
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(
            ListValue(
                null,
                IntegerValue(1, null),
                SymbolValue("a", null),
            ),
            (result as ListValue),
        )
    }

    @Test
    fun setCdrOfListToList() {
        val program =
            """
            (define l '(1 2 3))
            (set-cdr! l '(a b))
            l
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(
            ListValue(
                null,
                IntegerValue(1, null),
                SymbolValue("a", null),
                SymbolValue("b", null),
            ),
            (result as ListValue),
        )
    }

    @Test
    fun reverseFlatList() {
        val program =
            """
            (reverse '(a b c))
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(
            ListValue(
                null,
                SymbolValue("c", null),
                SymbolValue("b", null),
                SymbolValue("a", null),
            ),
            (result as ListValue),
        )
    }

    @Test
    fun reverseNestedList() {
        val program =
            """
            (reverse '(a (b c) d (e (f))))
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is ListValue)
        assertEquals(
            ListValue(
                null,
                ListValue(
                    null,
                    SymbolValue("e", null),
                    ListValue(null, SymbolValue("f", null)),
                ),
                SymbolValue("d", null),
                ListValue(
                    null,
                    SymbolValue("b", null),
                    SymbolValue("c", null),
                ),
                SymbolValue("a", null),
            ),
            (result as ListValue),
        )
    }
}
