abstract class SchemeValue {
    abstract override fun toString(): String
    abstract fun typeName(): String
}

class VoidValue() : SchemeValue() {
    override fun toString(): String {
        return "<#void>"
    }

    override fun typeName(): String {
        return "<#void>"
    }
}

data class BooleanValue(val value: Boolean) : SchemeValue() {
    override fun toString(): String {
        return value.toString()
    }

    override fun typeName(): String {
        return "<#boolean>"
    }
}

abstract class NumberValue() : SchemeValue() {
    abstract fun add(other: NumberValue): NumberValue
    abstract fun sub(other: NumberValue): NumberValue
    abstract fun smallerThan(other: NumberValue): BooleanValue
}

data class IntegerValue(val value: Int) : NumberValue() {
    override fun add(other: NumberValue): NumberValue {
        return when (other) {
            is IntegerValue -> IntegerValue(this.value + other.value)
            is FloatValue -> FloatValue(this.value + other.value)
            else -> TODO("Unsupported type")
        }
    }

    override fun sub(other: NumberValue): NumberValue {
        return when (other) {
            is IntegerValue -> IntegerValue(this.value - other.value)
            is FloatValue -> FloatValue(this.value - other.value)
            else -> TODO("Unsoported type")
        }
    }

    override fun smallerThan(other: NumberValue): BooleanValue {
        return when (other) {
            is IntegerValue -> BooleanValue(this.value < other.value)
            is FloatValue -> BooleanValue(this.value < other.value)
            else -> TODO("Unsoported type")
        }
    }

    override fun toString(): String {
        return value.toString()
    }

    override fun typeName(): String {
        return "<#integer>"
    }
}

data class FloatValue(val value: Float) : NumberValue() {
    override fun add(other: NumberValue): NumberValue {
        return when (other) {
            is IntegerValue -> FloatValue(this.value + other.value)
            is FloatValue -> FloatValue(this.value + other.value)
            else -> TODO("Unsoported type")
        }
    }

    override fun sub(other: NumberValue): NumberValue {
        return when (other) {
            is IntegerValue -> FloatValue(this.value - other.value)
            is FloatValue -> FloatValue(this.value - other.value)
            else -> TODO("Unsoported type")
        }
    }

    override fun smallerThan(other: NumberValue): BooleanValue {
        return when (other) {
            is IntegerValue -> BooleanValue(this.value < other.value)
            is FloatValue -> BooleanValue(this.value < other.value)
            else -> TODO("Unsoported type")
        }
    }

    override fun toString(): String {
        return value.toString()
    }

    override fun typeName(): String {
        return "<#float>"
    }
}

data class ListValue(val values: List<SchemeValue>) : SchemeValue() {
    override fun toString(): String {
        return "(" + values.joinToString(" ") { value -> value.toString() } + ")"
    }

    override fun typeName(): String {
        return "<#list>"
    }
}

data class FuncValue(val args: List<String>, val body: BodyNode, val env: Environment) : SchemeValue() {
    override fun toString(): String {
        // FIXME: Better display
        return "<Function>"
    }

    override fun typeName(): String {
        return "<#function>"
    }
}

data class NativeFuncArg(val value: SchemeValue, val location: Location)

data class NativeFuncValue(val name: String, val func: (List<NativeFuncArg>, Environment) -> SchemeValue) :
    SchemeValue() {
    override fun toString(): String {
        return "<Native Function $name>"
    }

    override fun typeName(): String {
        return "<#native func>"
    }
}
