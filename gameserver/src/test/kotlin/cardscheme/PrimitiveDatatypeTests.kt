package cardscheme

import org.junit.Assert
import org.junit.Test

class PrimitiveDatatypeTests {
    @Test
    fun simpleTrueBoolean() {
        var program = "#t"
        var result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)

        program = "#T"
        result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(true, (result as BooleanValue).value)
    }

    @Test
    fun simpleFalseBoolean() {
        var program = "#f"
        var result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)

        program = "#F"
        result = SchemeInterpreter().run(program)
        assert(result is BooleanValue)
        Assert.assertEquals(false, (result as BooleanValue).value)
    }
}
