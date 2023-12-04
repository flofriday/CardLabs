package cardscheme

import org.junit.Assert
import org.junit.Test

class CharacterTests {

    @Test
    fun simpleACharacter() {
        val program = """#\a"""
        val result = SchemeInterpreter().run(program)
        assert(result is CharacterValue)
        Assert.assertEquals('a', (result as CharacterValue).value)
    }

    @Test
    fun simpleUpperACharacter() {
        val program = """#\A"""
        val result = SchemeInterpreter().run(program)
        assert(result is CharacterValue)
        Assert.assertEquals('A', (result as CharacterValue).value)
    }
}

