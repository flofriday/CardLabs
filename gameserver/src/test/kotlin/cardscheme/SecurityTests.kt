package cardscheme

import org.junit.Assert
import org.junit.Test

class SecurityTests {
    @Test
    fun openFileShouldFail() {
        val program = """(open-input-file "Readme.md")"""
        Assert.assertThrows(SchemeError::class.java) { SchemeInterpreter().run(program) }
    }

    @Test
    fun importShouldFail() {
        val program = """(import (github.com/gambit/hello))
(hi "world")"""
        Assert.assertThrows(SchemeError::class.java) { SchemeInterpreter().run(program) }
    }

    @Test
    fun writingFileShouldFail() {
        val program = """(define output-file "output.txt")
    (with-output-to-file output-file
    (lambda ()
    (display "This is written to a file.")))"""
        Assert.assertThrows(SchemeError::class.java) { SchemeInterpreter().run(program) }
    }
}
