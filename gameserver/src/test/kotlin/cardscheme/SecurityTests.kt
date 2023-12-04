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

    @Test
    fun limitedMemoryTest() {
        val program = """(do ((i 1 (+ i 1)))
    ((> i 100))
  (+ i 2))
  (+ 1 5)
"""
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(6, (result as IntegerValue).value)

        Assert.assertThrows(SchemeError::class.java) { SchemeInterpreter(memoryLimit = 1).run(program) }
    }

    /*
    @Test(timeout = 30000)
    fun limitedMemoryTest2() {
        val program = """(define (infinite-append)
  (define empty-list (list ))

  (do ((acc empty-list (append acc (list 1)))
       (i 0 (+ i 1)))
      ((= i 1000000) acc)))
  (display (infinite-append))
"""

        Assert.assertThrows(SchemeError::class.java) { SchemeInterpreter(memoryLimit = 100000000).run(program) }
    }
     */
}
