package cardscheme

import org.junit.Assert
import org.junit.Test

class SchemeListTests {
    @Test
    fun creationEmptyList() {
        val list = SchemeList<Int>()
        Assert.assertEquals(0, list.size)
        Assert.assertEquals(false, list.remove(0))
    }

    @Test
    fun addToList() {
        val list = SchemeList<Int>()
        list.add(4)
        assert(!list.isEmpty())
        assert(list.contains(4))
        Assert.assertEquals(listOf(4), list.toList())
        Assert.assertEquals(1, list.size)
    }

    @Test
    fun addFirstToList() {
        val list = SchemeList<Int>()
        list.add(4)
        assert(!list.isEmpty())
        assert(list.contains(4))
        list.add(2)
        Assert.assertEquals(listOf(4, 2), list.toList())
        Assert.assertEquals(2, list.size)
        list.add(3)
        Assert.assertEquals(listOf(4, 2, 3), list.toList())
        Assert.assertEquals(3, list.size)
        list.addFirst(1)
        Assert.assertEquals(listOf(1, 4, 2, 3), list.toList())
        Assert.assertEquals(4, list.size)
    }

    @Test
    fun removeFromList() {
        val list = SchemeList<Int>(listOf(4, 2, 4))
        Assert.assertEquals(3, list.size)
        list.remove(4)
        Assert.assertEquals(listOf(2, 4), list.toList())
        Assert.assertEquals(2, list.size)
    }

    @Test
    fun createFromList() {
        val list = SchemeList<Int>(listOf(1, 2, 3, 4, 5, 6))
        Assert.assertEquals(6, list.size)
        Assert.assertEquals(listOf(1, 2, 3, 4, 5, 6), list.toList())
        assert(list.contains(6))
    }
}
