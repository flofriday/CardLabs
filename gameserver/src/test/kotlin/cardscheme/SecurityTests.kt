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

    // check if an infinitely long-running loop is cancelled after a timeout of 5 seconds
    @Test(timeout = 5 * 1000)
    fun infinitelyLongRunningLoopShouldBeCancelled() {
        val program = """(do ((i 1)) ((> i 2)))"""
        Assert.assertThrows(SchemeError::class.java) { SchemeInterpreter().run(program) }
    }
}
