package cardscheme

import org.junit.Assert
import org.junit.Test

class IterationTest {
    @Test
    fun iterationCountToHundred() {
        val program =
            """
            (do (
                    (i 0 (+ i 1)))
                ((>= i 100) i))
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(100, (result as IntegerValue).value)
    }

    @Test
    fun iterationWithoutReturn() {
        val program =
            """
            (do (
                    (i 0 (+ i 1)))
                ((>= i 100)))
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is VoidValue)
    }

    @Test
    fun iterationWithoutStep() {
        val program =
            """
            (do (
                    (i 42))
                ((>= i 10) i))
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(42, (result as IntegerValue).value)
    }

    // FIXME: There are tests missing to verify that the command gets executed every iteration, but at the time of
    // writing there aren't any good side-effect we could test for.
}
