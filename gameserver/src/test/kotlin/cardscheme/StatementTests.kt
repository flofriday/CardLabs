package cardscheme

import org.junit.Assert
import org.junit.Test

class StatementTests {
    @Test
    fun simpleDefine() {
        val program =
            """
            (define flo 42)
            flo
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(42, (result as IntegerValue).value)
    }

    @Test
    fun simpleDefineFunction() {
        val program =
            """
            (define (addone n) (+ n 1))
            (addone 41)
            """.trimMargin()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(42, (result as IntegerValue).value)
    }
}
