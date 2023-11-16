abstract class SchemeValue {
    abstract override fun toString(): String
}

class IntValue(val value: Int) : SchemeValue() {
    override fun toString(): String {
        return value.toString()
    }
}

class NativeFunc(val name: String, val func: (List<SchemeValue>, Environment) -> SchemeValue) : SchemeValue() {
    override fun toString(): String {
        return "<Native Function $name>"
    }
}