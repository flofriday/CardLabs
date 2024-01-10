package cardscheme

import org.junit.Assert
import org.junit.Test

class CommentTests {
    @Test
    fun smallerEqualWithManyInts3() {
        val program = """(+ 1 ;))))
                            2;(((
                            );some comment"""
        val result = SchemeInterpreter().run(program)
        assert(result is IntegerValue)
        Assert.assertEquals(3, (result as IntegerValue).value)
    }
}
