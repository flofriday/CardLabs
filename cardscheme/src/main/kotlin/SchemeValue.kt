abstract class SchemeValue {
    abstract override fun toString(): String
}

class IntValue(val value: Int) : SchemeValue() {
    override fun toString(): String {
        return value.toString()
    }
}