abstract class SchemeValue {
    abstract override fun toString(): String
}

class VoidValue() : SchemeValue() {
    override fun toString(): String {
        return "<#void>"
    }
}

data class BoolValue(val value: Boolean) : SchemeValue() {
    override fun toString(): String {
        return value.toString()
    }
}

data class IntValue(val value: Int) : SchemeValue() {
    override fun toString(): String {
        return value.toString()
    }
}

data class ListValue(val values: List<SchemeValue>) : SchemeValue() {
    override fun toString(): String {
        return "(" + values.joinToString(" ") { value -> value.toString() } + ")"
    }
}

data class NativeFuncValue(val name: String, val func: (List<SchemeValue>, Environment) -> SchemeValue) :
    SchemeValue() {
    override fun toString(): String {
        return "<Native Function $name>"
    }
}

data class FuncValue(val args: List<String>, val body: List<ExpressionNode>, val env: Environment) : SchemeValue() {
    override fun toString(): String {
        // FIXME: Better display
        return "<Function>"
    }
}