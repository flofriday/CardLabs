package cardscheme

import org.junit.Assert
import org.junit.Test


class SymbolTests {
    @Test
    fun symbolFromQuote() {
        val program = """(quote flo)"""
        val result = SchemeInterpreter().run(program)
        assert(result is SymbolValue)
        Assert.assertEquals("flo", (result as SymbolValue).value)
    }

    @Test
    fun symbolFromSingleQuote() {
        val program = """'flo"""
        val result = SchemeInterpreter().run(program)
        assert(result is SymbolValue)
        Assert.assertEquals("flo", (result as SymbolValue).value)
    }

    @Test
    fun isSymbolOnSymbol() {
        val program = """(symbol? 'flo)"""
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun isSymbolOnString() {
        val program = """(symbol? "flo")"""
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

    @Test
    fun isSymbolEqualOnEqual() {
        val program = """(symbol=? 'dog 'dog 'dog)"""
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun isSymbolEqualOnDifferent() {
        val program = """(symbol=? 'dog 'dog 'cat)"""
        val result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }

}
