import java.util.LinkedList

abstract class SchemeValue {
    abstract override fun toString(): String

    abstract fun typeName(): String

    /**
     * Determines wether or not a value is considerd as "true".
     *
     * Spec: R7R, chapter 6.3
     * Of all the Scheme values, only #f counts as false in conditional
     * expressions. All other Scheme values, including #t, count as true.
     */
    fun isTruthy(): Boolean {
        if (this is BooleanValue) {
            return this.value
        }
        return true
    }
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
        return if (value) "#t" else "#f"
    }

    override fun typeName(): String {
        return "<#boolean>"
    }
}

abstract class NumberValue() : SchemeValue() {
    abstract fun add(other: NumberValue): NumberValue

    abstract fun sub(other: NumberValue): NumberValue

    abstract fun mul(other: NumberValue): NumberValue

    abstract fun div(other: NumberValue): NumberValue

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

    override fun mul(other: NumberValue): NumberValue {
        return when (other) {
            is IntegerValue -> IntegerValue(this.value * other.value)
            is FloatValue -> FloatValue(this.value * other.value)
            else -> TODO("Unsupported type")
        }
    }

    override fun div(other: NumberValue): NumberValue {
        return when (other) {
            is IntegerValue -> IntegerValue(this.value / other.value)
            is FloatValue -> FloatValue(this.value / other.value)
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

    override fun mul(other: NumberValue): NumberValue {
        return when (other) {
            is IntegerValue -> FloatValue(this.value * other.value)
            is FloatValue -> FloatValue(this.value * other.value)
            else -> TODO("Unsoported type")
        }
    }

    override fun div(other: NumberValue): NumberValue {
        return when (other) {
            is IntegerValue -> FloatValue(this.value / other.value)
            is FloatValue -> FloatValue(this.value / other.value)
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

// FIXME: We probably call quite often cdr (tail) on the list but from what
// I can see we can only implement that in Java in O(n) even though that should
// be quite easy to do in O(1). So we probably need to write our own.
data class ListValue(val values: LinkedList<SchemeValue>) : SchemeValue() {
    override fun toString(): String {
        return "(" + values.joinToString(" ") { value -> value.toString() } + ")"
    }

    override fun typeName(): String {
        return "<#list>"
    }
}

/**
 * A vector data structure.
 *
 * Spec: R7R, chapter 6.8
 */
data class VectorValue(val values: MutableList<SchemeValue>) : SchemeValue() {
    override fun toString(): String {
        return "#(" + values.joinToString(" ") { value -> value.toString() } + ")"
    }

    override fun typeName(): String {
        return "<#vector>"
    }
}

data class Arity(val min: Int, val max: Int) {
    fun inside(n: Int): Boolean {
        return n in min..max
    }
}

abstract class CallableValue(open val arity: Arity) : SchemeValue()

data class FuncValue(
    val params: List<String>,
    override val arity: Arity,
    val body: BodyNode,
    val env: Environment,
) : CallableValue(arity) {
    override fun toString(): String {
        // FIXME: Better display
        return "<Function>"
    }

    override fun typeName(): String {
        return "<#function>"
    }
}

data class FuncArg(val value: SchemeValue, val location: Location?)

data class NativeFuncValue(
    val name: String,
    override val arity: Arity,
    val func: (List<FuncArg>, Executor) -> SchemeValue,
) : CallableValue(arity) {
    override fun toString(): String {
        return "<Native Function $name>"
    }

    override fun typeName(): String {
        return "<#native func>"
    }
}
