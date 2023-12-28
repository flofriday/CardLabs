@file:Suppress("removal")

package cardscheme

import kotlin.math.absoluteValue

abstract class SchemeValue {

    abstract override fun toString(): String

    abstract fun typeName(): String

    /**
     * Determines wether or not a value is considerd as "true".
     *
     * Spec: R7RS, chapter 6.3
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

class VoidValue(val schemeSecurityMonitor: SchemeSecurityMonitor?) : SchemeValue() {

    init {
        schemeSecurityMonitor?.allocate()
    }

    protected fun finalize() {
        schemeSecurityMonitor?.free()
    }

    override fun toString(): String {
        return "<#void>"
    }

    override fun typeName(): String {
        return "<#void>"
    }
}

data class BooleanValue(val value: Boolean, val schemeSecurityMonitor: SchemeSecurityMonitor?) : SchemeValue() {
    init {
        schemeSecurityMonitor?.allocate()
    }

    protected fun finalize() {
        schemeSecurityMonitor?.free()
    }

    override fun toString(): String {
        return if (value) "#t" else "#f"
    }

    override fun typeName(): String {
        return "<#boolean>"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BooleanValue

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}

abstract class NumberValue() : SchemeValue() {

    abstract fun add(other: NumberValue): NumberValue

    abstract fun sub(other: NumberValue): NumberValue

    abstract fun mul(other: NumberValue): NumberValue

    abstract fun div(other: NumberValue): NumberValue

    // Numerical equality
    abstract fun numEqual(other: NumberValue): BooleanValue

    abstract fun smallerThan(other: NumberValue): BooleanValue

    abstract fun abs(): NumberValue

    abstract fun sqrt(): NumberValue
}

data class IntegerValue(val value: Int, val schemeSecurityMonitor: SchemeSecurityMonitor?) : NumberValue() {

    init {
        schemeSecurityMonitor?.allocate()
    }

    protected fun finalize() {
        schemeSecurityMonitor?.free()
    }

    override fun add(other: NumberValue): NumberValue {
        return when (other) {
            is IntegerValue -> IntegerValue(this.value + other.value, schemeSecurityMonitor)
            is FloatValue -> FloatValue(this.value + other.value, schemeSecurityMonitor)
            else -> TODO("Unsupported type")
        }
    }

    override fun sub(other: NumberValue): NumberValue {
        return when (other) {
            is IntegerValue -> IntegerValue(this.value - other.value, schemeSecurityMonitor)
            is FloatValue -> FloatValue(this.value - other.value, schemeSecurityMonitor)
            else -> TODO("Unsoported type")
        }
    }

    override fun mul(other: NumberValue): NumberValue {
        return when (other) {
            is IntegerValue -> IntegerValue(this.value * other.value, schemeSecurityMonitor)
            is FloatValue -> FloatValue(this.value * other.value, schemeSecurityMonitor)
            else -> TODO("Unsupported type")
        }
    }

    override fun div(other: NumberValue): NumberValue {
        return when (other) {
            is IntegerValue -> {
                if (this.value % other.value == 0) {
                    IntegerValue(this.value / other.value, schemeSecurityMonitor)
                } else {
                    FloatValue(this.value.toFloat() / other.value, schemeSecurityMonitor)
                }
            }

            is FloatValue -> FloatValue(this.value / other.value, schemeSecurityMonitor)
            else -> TODO("Unsoported type")
        }
    }

    override fun numEqual(other: NumberValue): BooleanValue {
        return when (other) {
            is IntegerValue -> BooleanValue(this.value == other.value, schemeSecurityMonitor)
            is FloatValue -> BooleanValue(this.value.toFloat() == other.value, schemeSecurityMonitor)
            else -> TODO("Unsoported type")
        }
    }

    override fun smallerThan(other: NumberValue): BooleanValue {
        return when (other) {
            is IntegerValue -> BooleanValue(this.value < other.value, schemeSecurityMonitor)
            is FloatValue -> BooleanValue(this.value < other.value, schemeSecurityMonitor)
            else -> TODO("Unsoported type")
        }
    }

    override fun abs(): IntegerValue {
        return IntegerValue(value.absoluteValue, schemeSecurityMonitor)
    }

    override fun sqrt(): FloatValue {
        return FloatValue(kotlin.math.sqrt(value.toFloat()), schemeSecurityMonitor)
    }

    override fun toString(): String {
        return value.toString()
    }

    override fun typeName(): String {
        return "<#integer>"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IntegerValue

        return value == other.value
    }

    override fun hashCode(): Int {
        return value
    }
}

data class FloatValue(val value: Float, val schemeSecurityMonitor: SchemeSecurityMonitor?) : NumberValue() {
    init {
        schemeSecurityMonitor?.allocate()
    }

    protected fun finalize() {
        schemeSecurityMonitor?.free()
    }

    override fun add(other: NumberValue): NumberValue {
        return when (other) {
            is IntegerValue -> FloatValue(this.value + other.value, schemeSecurityMonitor)
            is FloatValue -> FloatValue(this.value + other.value, schemeSecurityMonitor)
            else -> TODO("Unsoported type")
        }
    }

    override fun sub(other: NumberValue): NumberValue {
        return when (other) {
            is IntegerValue -> FloatValue(this.value - other.value, schemeSecurityMonitor)
            is FloatValue -> FloatValue(this.value - other.value, schemeSecurityMonitor)
            else -> TODO("Unsoported type")
        }
    }

    override fun mul(other: NumberValue): NumberValue {
        return when (other) {
            is IntegerValue -> FloatValue(this.value * other.value, schemeSecurityMonitor)
            is FloatValue -> FloatValue(this.value * other.value, schemeSecurityMonitor)
            else -> TODO("Unsoported type")
        }
    }

    override fun div(other: NumberValue): NumberValue {
        return when (other) {
            is IntegerValue -> FloatValue(this.value / other.value, schemeSecurityMonitor)
            is FloatValue -> FloatValue(this.value / other.value, schemeSecurityMonitor)
            else -> TODO("Unsoported type")
        }
    }

    override fun numEqual(other: NumberValue): BooleanValue {
        return when (other) {
            is IntegerValue -> BooleanValue(this.value == other.value.toFloat(), schemeSecurityMonitor)
            is FloatValue -> BooleanValue(this.value == other.value, schemeSecurityMonitor)
            else -> TODO("Unsoported type")
        }
    }

    override fun smallerThan(other: NumberValue): BooleanValue {
        return when (other) {
            is IntegerValue -> BooleanValue(this.value < other.value, schemeSecurityMonitor)
            is FloatValue -> BooleanValue(this.value < other.value, schemeSecurityMonitor)
            else -> TODO("Unsoported type")
        }
    }

    override fun abs(): FloatValue {
        return FloatValue(value.absoluteValue, schemeSecurityMonitor)
    }

    override fun sqrt(): FloatValue {
        return FloatValue(kotlin.math.sqrt(value), schemeSecurityMonitor)
    }

    override fun toString(): String {
        return value.toString()
    }

    override fun typeName(): String {
        return "<#float>"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FloatValue

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}

data class StringValue(val value: String, val schemeSecurityMonitor: SchemeSecurityMonitor?) : SchemeValue() {

    init {
        schemeSecurityMonitor?.allocate()
    }

    protected fun finalize() {
        schemeSecurityMonitor?.free()
    }

    override fun toString(): String {
        return '"' + value + '"'
    }

    fun toPureString(): String {
        return value
    }

    override fun typeName(): String {
        return "<#string>"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StringValue

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}

data class CharacterValue(val value: Char, val schemeSecurityMonitor: SchemeSecurityMonitor?) : SchemeValue() {
    init {
        schemeSecurityMonitor?.allocate()
    }

    protected fun finalize() {
        schemeSecurityMonitor?.free()
    }
    override fun toString(): String {
        return "#\\$value"
    }

    override fun typeName(): String {
        return "<#char>"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CharacterValue

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}

data class SymbolValue(val value: String, val schemeSecurityMonitor: SchemeSecurityMonitor?) : SchemeValue() {
    init {
        schemeSecurityMonitor?.allocate()
    }

    protected fun finalize() {
        schemeSecurityMonitor?.free()
    }
    override fun toString(): String {
        return value
    }

    override fun typeName(): String {
        return "<#symbol>"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SymbolValue

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}

// FIXME: We probably call quite often cdr (tail) on the list but from what
// I can see we can only implement that in Java in O(n) even though that should
// be quite easy to do in O(1). So we probably need to write our own.
data class ListValue(val values: SchemeList<SchemeValue>, val schemeSecurityMonitor: SchemeSecurityMonitor?) : SchemeValue() {

    init {
        schemeSecurityMonitor?.allocate(values.size.toLong())
    }

    protected fun finalize() {
        schemeSecurityMonitor?.free()
    }
    constructor(valueList: List<SchemeValue>, schemeSecurityMonitor: SchemeSecurityMonitor?) : this(SchemeList(valueList), schemeSecurityMonitor) {
    }

    constructor(schemeSecurityMonitor: SchemeSecurityMonitor?, vararg valueList: SchemeValue) : this(SchemeList(valueList.toList()), schemeSecurityMonitor) {
    }

    override fun toString(): String {
        return "(" + values.joinToString(" ") { value -> value.toString() } + ")"
    }

    override fun typeName(): String {
        return "<#list>"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ListValue

        return values == other.values
    }

    override fun hashCode(): Int {
        return values.hashCode()
    }
}

/**
 * A vector data structure.
 *
 * Spec: R7RS, chapter 6.8
 */
data class VectorValue(val values: MutableList<SchemeValue>, val schemeSecurityMonitor: SchemeSecurityMonitor?) : SchemeValue() {
    init {
        schemeSecurityMonitor?.allocate(values.size.toLong())
    }

    protected fun finalize() {
        schemeSecurityMonitor?.free()
    }
    override fun toString(): String {
        return "#(" + values.joinToString(" ") { value -> value.toString() } + ")"
    }

    override fun typeName(): String {
        return "<#vector>"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VectorValue

        return values == other.values
    }

    override fun hashCode(): Int {
        return values.hashCode()
    }
}

data class Arity(val min: Int, val max: Int, val isVarArg: Boolean) {
    fun inside(n: Int): Boolean {
        if (isVarArg) {
            return n >= min
        }

        return n in min..max
    }
}

abstract class CallableValue(open val arity: Arity) : SchemeValue()

data class FuncValue(
    val params: List<String>,
    override val arity: Arity,
    val body: BodyNode,
    val env: Environment,
    val schemeSecurityMonitor: SchemeSecurityMonitor,
) : CallableValue(arity) {

    init {
        schemeSecurityMonitor.allocate()
    }

    protected fun finalize() {
        schemeSecurityMonitor.free()
    }

    override fun toString(): String {
        // FIXME: Better display
        return "<Function>"
    }

    override fun typeName(): String {
        return "<#function>"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FuncValue

        if (params != other.params) return false
        if (arity != other.arity) return false
        if (body != other.body) return false
        if (env != other.env) return false

        return true
    }

    override fun hashCode(): Int {
        var result = params.hashCode()
        result = 31 * result + arity.hashCode()
        result = 31 * result + body.hashCode()
        result = 31 * result + env.hashCode()
        return result
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
