class SchemeList<T>() : MutableCollection<T> {
    override var size = 0
    private var node: SchemeListNode<T>? = null

    constructor(element: T) : this() {
        this.add(element)
    }

    private constructor(node: SchemeListNode<T>?, size: Int) : this() {
        this.node = node
        this.size = size
    }

    constructor(elements: Collection<T>) : this() {
        this.addAll(elements)
    }

    override fun clear() {
        node = null
        size = 0
    }

    fun tail(): SchemeList<T> {
        if (node == null) {
            return SchemeList<T>()
        }
        return SchemeList<T>(node!!.next, size - 1)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        // FIXME: THis is currently in O(n^2) but could be in O(n)
        return elements.all { e -> this.add(e) }
    }

    override fun add(element: T): Boolean {
        size++
        if (node == null) {
            node = SchemeListNode(element, null)
            return true
        }
        return node!!.add(element)
    }

    override fun isEmpty(): Boolean {
        return node == null
    }

    override fun iterator(): MutableIterator<T> {
        return SchemeListIterator(this)
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        var hasRemoved = false
        for (value in this) {
            if (!elements.contains(value)) {
                hasRemoved = true
                while (remove(value)) {}
            }
        }
        return hasRemoved
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        return elements.any { e -> remove(e) }
    }

    override fun remove(element: T): Boolean {
        if (node == null) {
            return false
        }

        var current = node!!
        if (current.value == element) {
            node = current.next
            size--
            return true
        }

        while (current.next != null) {
            if (current.next!!.value == element) {
                current.next = current.next!!.next
                size--
                return true
            }
            current = current.next!!
        }
        return false
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        return elements.all { e -> this.contains(e) }
    }

    override fun contains(element: T): Boolean {
        for (item in this) {
            if (item == element) return true
        }
        return false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SchemeList<*>

        if (size != other.size) return false
        if (node != other.node) return false

        return true
    }

    class SchemeListIterator<T>(var list: SchemeList<T>) : MutableIterator<T> {
        private var previousPreviousNode = list.node
        private var previousNode = list.node
        private var currentNode = list.node

        override fun hasNext(): Boolean {
            return currentNode != null
        }

        override fun next(): T {
            val result = currentNode!!.value
            previousPreviousNode = previousNode
            previousNode = currentNode
            currentNode = currentNode!!.next
            return result
        }

        override fun remove() {
            if (currentNode === list.node!!.next) {
                list.node = currentNode
                list.size -= 1
                return
            }

            previousPreviousNode!!.next = currentNode
        }
    }
}

class SchemeListNode<T>(var value: T, var next: SchemeListNode<T>?) {
    fun add(element: T): Boolean {
        if (next == null) {
            next = SchemeListNode(element, null)
            return true
        }
        return next!!.add(element)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SchemeListNode<*>

        return (value == other.value) && (next == other.next)
    }
}
