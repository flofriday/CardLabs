package cardscheme

import org.junit.Assert
import org.junit.Test

class VariableTests {
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

    @Test
    fun simpleReDefine() {
        val program =
            """
            (define n 42)
            (define n 13)
            n
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(13, (result as IntegerValue).value)
    }

    @Test
    fun simpleDefineUpdate() {
        val program =
            """
            (define n 42)
            (set! n 13)
            n
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(13, (result as IntegerValue).value)
    }

    @Test
    fun defineNested() {
        val program =
            """
                ; At the end g should not be updated
                (define g 2)
                
                (define (badUpdateGlobal n) 
                    (define g n)
                    n)
                   
                (badUpdateGlobal 3)
                g
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(2, (result as IntegerValue).value)
    }

    @Test
    fun defineUpdateNested() {
        val program =
            """
                ; At the end g should be updated
                (define g 2)
                
                (define (correctUpdateGlobal n) 
                    (set! g n)
                    n)
                   
                (correctUpdateGlobal 3)
                g
            """.trimIndent()
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(3, (result as IntegerValue).value)
    }
}
