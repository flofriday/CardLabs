package cardscheme

import org.junit.Assert
import org.junit.Assert.assertThrows
import org.junit.Test

class StringTests {
    @Test
    fun stringAppend() {
        val program = """(string-append "abc" "efg")"""
        val result = SchemeInterpreter().run(program)
        assert(result is StringValue)
        Assert.assertEquals("abcefg", (result as StringValue).value)
    }

    @Test
    fun stringAppendWithMany() {
        val program = """(string-append "abcd" "efg" "hi" "jklmno" "p")"""
        val result = SchemeInterpreter().run(program)
        assert(result is StringValue)
        Assert.assertEquals("abcdefghijklmnop", (result as StringValue).value)
    }

    @Test
    fun numberToString() {
        val program = "(number->string 4)"
        val result = SchemeInterpreter().run(program)
        assert(result is StringValue)
        Assert.assertEquals("4", (result as StringValue).value)
    }

    @Test
    fun numberToString2() {
        val program = "(number->string 373278)"
        val result = SchemeInterpreter().run(program)
        assert(result is StringValue)
        Assert.assertEquals("373278", (result as StringValue).value)
    }

    @Test
    fun numberToStringWithBool() {
        val program = "(number->string #t)"
        assertThrows(SchemeError::class.java) { SchemeInterpreter().run(program) }
    }

    @Test
    fun stringToInt() {
        val program = """(string->number "100")"""
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(100, (result as IntegerValue).value)
    }

    @Test
    fun stringToFloat() {
        val program = """(string->number "50.3456")"""
        val result = SchemeInterpreter().run(program)
        assert(result is FloatValue)
        Assert.assertEquals(50.3456f, (result as FloatValue).value)
    }

    @Test
    fun stringToFloatTrailingZeros() {
        val program = """(string->number "50.3456000")"""
        val result = SchemeInterpreter().run(program)
        assert(result is FloatValue)
        Assert.assertEquals(50.3456f, (result as FloatValue).value)
    }

    @Test
    fun symbolToString() {
        val program = """(symbol->string 'test)"""
        val result = SchemeInterpreter().run(program)
        assert(result is StringValue)
        Assert.assertEquals("test", (result as StringValue).value)
    }

    @Test
    fun stringToSymbol() {
        val program = """(string->symbol "paul")"""
        val result = SchemeInterpreter().run(program)
        assert(result is SymbolValue)
        Assert.assertEquals("paul", (result as SymbolValue).value)
    }

    @Test
    fun stringLength() {
        val program = """(string-length "AB")"""
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(2, (result as IntegerValue).value)
    }

    @Test
    fun stringLengthTooManyArguments() {
        val program = """(string-length "AB" "AB")"""
        assertThrows(SchemeError::class.java) { SchemeInterpreter().run(program) }
    }

    @Test
    fun makeString() {
        val program = """(make-string 4 #\a)"""
        val result = SchemeInterpreter().run(program)
        assert(result is StringValue)
        Assert.assertEquals("aaaa", (result as StringValue).value)
    }
}
