abstract class SchemeValue {
    abstract fun display(): String
}

class IntValue(val value: Int): SchemeValue() {
    override fun display(): String {
        return value.toString()
    }
}