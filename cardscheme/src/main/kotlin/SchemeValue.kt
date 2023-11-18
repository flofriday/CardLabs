abstract class SchemeValue {
    abstract override fun toString(): String
}

data class IntValue(val value: Int) : SchemeValue() {
    override fun toString(): String {
        return value.toString()
    }
}

data class ListValue(val values: List<SchemeValue>) : SchemeValue() {
    override fun toString(): String {
        return "(" + values.joinToString(" "){ value -> value.toString() } + ")"
    }
}

data class NativeFunc(val name: String, val func: (List<SchemeValue>, Environment) -> SchemeValue) : SchemeValue() {
    override fun toString(): String {
        return "<Native Function $name>"
    }
}