import java.util.LinkedList

fun injectBuiltin(environment: Environment) {
    environment.put("+", NativeFuncValue("+", Arity(2, Int.MAX_VALUE), ::builtinPlus))
    environment.put("-", NativeFuncValue("-", Arity(2, Int.MAX_VALUE), ::builtinMinus))
    environment.put("*", NativeFuncValue("*", Arity(2, Int.MAX_VALUE), ::builtinMul))
    environment.put("/", NativeFuncValue("/", Arity(2, Int.MAX_VALUE), ::builtinDiv))

    environment.put("=", NativeFuncValue("=", Arity(2, Int.MAX_VALUE), ::builtinEqual))
    environment.put("<", NativeFuncValue("<", Arity(2, Int.MAX_VALUE), ::builtinSmaller))
    environment.put("<=", NativeFuncValue("<=", Arity(2, Int.MAX_VALUE), ::builtinSmallerEqual))
    environment.put(">", NativeFuncValue(">", Arity(2, Int.MAX_VALUE), ::builtinGreater))
    environment.put(">=", NativeFuncValue(">=", Arity(2, Int.MAX_VALUE), ::builtinGreaterEqual))

    environment.put("and", NativeFuncValue("and", Arity(0, Int.MAX_VALUE), ::builtinAnd))
    environment.put("or", NativeFuncValue("or", Arity(0, Int.MAX_VALUE), ::builtinOr))
    environment.put("not", NativeFuncValue("not", Arity(1, 1), ::builtinNot))

    environment.put("list", NativeFuncValue("list", Arity(0, Int.MAX_VALUE), ::builtinList))
    environment.put("car", NativeFuncValue("car", Arity(1, 1), ::builtinCar))
    environment.put("cdr", NativeFuncValue("cdr", Arity(1, 1), ::builtinCdr))
    environment.put("map", NativeFuncValue("map", Arity(2, Int.MAX_VALUE), ::builtinMap))

    environment.put("vector", NativeFuncValue("vector", Arity(1, Int.MAX_VALUE), ::builtinVector))
    environment.put("vector?", NativeFuncValue("vector?", Arity(1, 1), ::builtinIsVector))
    environment.put("make-vector", NativeFuncValue("make-vector", Arity(1, 2), ::builtinMakeVector))
    environment.put("vector-length", NativeFuncValue("vector-length", Arity(1, 1), ::builtinVectorLength))
    environment.put("vector-ref", NativeFuncValue("vector-ref", Arity(2, 2), ::builtinVectorRef))
    environment.put("vector-set!", NativeFuncValue("vector-set!", Arity(3, 3), ::builtinVectorSet))

    environment.put("string-append", NativeFuncValue("string-append", Arity(0, Int.MAX_VALUE), ::builtinStringAppend))
    environment.put("string->number", NativeFuncValue("string->number", Arity(1, 1), ::builtinStringToNumber))
    environment.put("number->string", NativeFuncValue("number->string", Arity(1, 1), ::builtinNumberToString))

    environment.put("display", NativeFuncValue("display", Arity(1, 1), ::builtinDisplay))
    environment.put("newline", NativeFuncValue("newline", Arity(0, 0), ::builtinNewline))
    environment.put("cool", NativeFuncValue("cool", Arity(0, 0), ::builtinCool))
}

inline fun <reified T : SchemeValue> verifyType(
    arg: FuncArg,
    expectedMsg: String,
): T {
    if (arg.value !is T) {
        throw SchemeError(
            "Unsupported Type",
            "$expectedMsg, but this argument is a ${arg.value.typeName()}",
            arg.location,
            null,
        )
    }
    return arg.value
}

inline fun <reified T : SchemeValue> verifyAllType(
    args: List<FuncArg>,
    expectedMsg: String,
): List<T> {
    return args.map { a -> verifyType<T>(a, expectedMsg) }
}

fun builtinPlus(
    args: List<FuncArg>,
    executor: Executor,
): NumberValue {
    verifyAllType<NumberValue>(args, "Only numbers can be added")
    return args.map { a -> a.value as NumberValue }.reduce { sum, n -> sum.add(n) }
}

fun builtinMinus(
    args: List<FuncArg>,
    executor: Executor,
): SchemeValue {
    verifyAllType<NumberValue>(args, "Only numbers can be subtracted")
    return args.map { a -> a.value as NumberValue }.reduce { res, n -> res.sub(n) }
}

fun builtinMul(
    args: List<FuncArg>,
    executor: Executor,
): NumberValue {
    verifyAllType<NumberValue>(args, "Only numbers can be multiplied")
    return args.map { a -> a.value as NumberValue }.reduce { res, n -> res.mul(n) }
}

fun builtinDiv(
    args: List<FuncArg>,
    executor: Executor,
): NumberValue {
    verifyAllType<NumberValue>(args, "Only numbers can be divided")
    return args.map { a -> a.value as NumberValue }.reduce { res, n -> res.div(n) }
}

fun builtinList(
    args: List<FuncArg>,
    executor: Executor,
): SchemeValue {
    return ListValue(LinkedList(args.map { a -> a.value }))
}

fun builtinCar(
    args: List<FuncArg>,
    executor: Executor,
): SchemeValue {
    verifyType<ListValue>(args.first(), "I expected a list here")
    val list = args.first().value as ListValue

    if (list.values.isEmpty()) {
        throw SchemeError("Empty List", "Car can only be called on non-empty lists", args.first().location, null)
    }

    return list.values.first()
}

fun builtinCdr(
    args: List<FuncArg>,
    executor: Executor,
): ListValue {
    verifyType<ListValue>(args.first(), "I expected a list here")
    val list = args.first().value as ListValue

    if (list.values.isEmpty()) {
        throw SchemeError("Empty List", "Cdr can only be called on non-empty lists", args.first().location, null)
    }

    // FIXME: Is there a better solution?
    return ListValue(LinkedList(list.values.drop(1)))
}

/**
 * Built in map
 *
 * Spec: R7R, Chapter 6.10
 * Syntax: (map proc list1 list2 ...)
 * */
fun builtinMap(
    args: List<FuncArg>,
    executor: Executor,
): ListValue {
    val func = verifyType<CallableValue>(args.first(), "The first argument must be a procedure")
    val lists = verifyAllType<ListValue>(args.drop(1), "All arguments after the first one must be lists")

    val values = LinkedList<SchemeValue>()
    for (i in 0..<lists.map { l -> l.values.size }.min()) {
        val iterationArgs = lists.map { l -> FuncArg(l.values[i], null) }
        values.addLast(executor.callFunction(func, iterationArgs))
    }
    return ListValue(values)
}

fun builtinVector(
    args: List<FuncArg>,
    executor: Executor,
): VectorValue {
    return VectorValue(args.map { a -> a.value }.toMutableList())
}

fun builtinIsVector(
    args: List<FuncArg>,
    executor: Executor,
): BooleanValue {
    return BooleanValue(args.first().value is VectorValue)
}

fun builtinMakeVector(
    args: List<FuncArg>,
    executor: Executor,
): VectorValue {
    val k = verifyType<IntegerValue>(args.first(), "Only positive integers can be used to specify the length of a vector")
    if (k.value <= 0) {
        throw SchemeError(
            "Type Mismatch",
            "Only positive integers can be used to specify the length of a vector, but the value was ${k.value}.",
            args.first().location,
            null,
        )
    }

    val filler =
        if (args.size == 2) {
            args[1].value
        } else {
            VoidValue()
        }
    return VectorValue((1..k.value).map { filler }.toMutableList())
}

fun builtinVectorLength(
    args: List<FuncArg>,
    executor: Executor,
): IntegerValue {
    val vec = verifyType<VectorValue>(args.first(), "Only vectors are expected here")
    return IntegerValue(vec.values.size)
}

fun builtinVectorRef(
    args: List<FuncArg>,
    executor: Executor,
): SchemeValue {
    val vec = verifyType<VectorValue>(args.first(), "Only vectors are expected here")
    val k = verifyType<IntegerValue>(args[1], "The index must be an integer")

    if (k.value < 0 || k.value >= vec.values.size) {
        throw SchemeError(
            "Invalid Index",
            "The vector has ${vec.values.size} elements but you tried to access index ${k.value}.",
            args[1].location,
            null,
        )
    }

    return vec.values[k.value]
}

fun builtinVectorSet(
    args: List<FuncArg>,
    executor: Executor,
): VoidValue {
    val vec = verifyType<VectorValue>(args.first(), "Only vectors are expected here")
    val k = verifyType<IntegerValue>(args[1], "The index must be an integer")
    val obj = args[2].value

    if (k.value < 0 || k.value >= vec.values.size) {
        throw SchemeError(
            "Invalid Index",
            "The vector has ${vec.values.size} elements but you tried to access index ${k.value}.",
            args[1].location,
            null,
        )
    }

    vec.values[k.value] = obj

    return VoidValue()
}

fun builtinEqual(
    args: List<FuncArg>,
    executor: Executor,
): BooleanValue {
    for ((value, loc) in args) {
        if (value.javaClass != args[0].value::class.java) {
            throw SchemeError(
                "Expected same type",
                "Expected all arguments to be of the same type",
                loc,
                null,
            )
        }
    }

    val result = args.map { a -> a.value }.zipWithNext { a, b -> a == b }.all { it }
    return BooleanValue(result)
}

/**
 * Built in string append
 *
 * Spec: R7R, Chapter 6.7
 * Syntax: (string-append string ...)
 * */
fun builtinStringAppend(
    args: List<FuncArg>,
    executor: Executor,
): StringValue {
    verifyAllType<StringValue>(args, "Only strings can be appended")
    return StringValue(args.joinToString("") { a -> (a.value as StringValue).value })
}

/**
 * Built in number to string conversion
 *
 * Spec: R7R, Chapter 6.2.7
 * Syntax: (number->string z)
 *         (number->string z radix ) //FIXME: Not implemented
 * */
fun builtinNumberToString(
    args: List<FuncArg>,
    executor: Executor,
): StringValue {
    verifyType<NumberValue>(args[0], "Only numbers can be converted to string by this function")
    return StringValue(args[0].value.toString())
}

/**
 * Built in string to number conversion
 *
 * Spec: R7R, Chapter 6.2.7
 * Syntax: (string->number string)
 *         (number->string z radix ) //FIXME: Not implemented
 * */
fun builtinStringToNumber(
    args: List<FuncArg>,
    executor: Executor,
): SchemeValue {
    verifyType<StringValue>(args[0], "Only strings can be converted to numbers by this function")
    val stringValue = (args[0].value as StringValue).value
    if (stringValue.contains(".")) {
        return FloatValue(stringValue.toFloat())
    }

    return IntegerValue(stringValue.toInt())
}

fun builtinSmallerEqual(
    args: List<FuncArg>,
    executor: Executor,
): BooleanValue {
    verifyAllType<NumberValue>(args, "Only numbers can be compared")

    val result =
        args
            .map { a -> a.value as NumberValue }
            .zipWithNext { a, b -> a.smallerThan(b).value || a == b }
            .all { it }
    return BooleanValue(result)
}

fun builtinSmaller(
    args: List<FuncArg>,
    executor: Executor,
): BooleanValue {
    verifyAllType<NumberValue>(args, "Only numbers can be compared")

    val result =
        args
            .map { a -> a.value as NumberValue }
            .zipWithNext { a, b -> a.smallerThan(b) }
            .all { it.value }
    return BooleanValue(result)
}

fun builtinGreater(
    args: List<FuncArg>,
    executor: Executor,
): BooleanValue {
    verifyAllType<NumberValue>(args, "Only numbers can be compared")

    val result =
        args
            .map { a -> a.value as NumberValue }
            .zipWithNext { a, b -> a.smallerThan(b).value || a == b }
            .none { it }
    return BooleanValue(result)
}

fun builtinGreaterEqual(
    args: List<FuncArg>,
    executor: Executor,
): BooleanValue {
    verifyAllType<NumberValue>(args, "Only numbers can be compared")

    val result =
        args
            .map { a -> a.value as NumberValue }
            .zipWithNext { a, b -> !a.smallerThan(b).value || a == b }
            .all { it }
    return BooleanValue(result)
}

/**
 * Built in logical and
 *
 * Spec: R7R, Chapter 4.2.1
 * */
fun builtinAnd(
    args: List<FuncArg>,
    executor: Executor,
): SchemeValue {
    if (args.isEmpty()) {
        return BooleanValue(true)
    }
    for (arg in args) {
        if (!arg.value.isTruthy()) {
            return BooleanValue(false)
        }
    }
    return args.last().value
}

/**
 * Built in logical or
 *
 * Spec: R7R, Chapter 4.2.1
 * */
fun builtinOr(
    args: List<FuncArg>,
    executor: Executor,
): SchemeValue {
    if (args.isEmpty()) {
        return BooleanValue(false)
    }
    for (arg in args) {
        if (arg.value.isTruthy()) {
            return arg.value
        }
    }
    return BooleanValue(false)
}

/**
 * Built in logical not
 *
 * Spec: R7R, Chapter 6.3
 * */
fun builtinNot(
    args: List<FuncArg>,
    executor: Executor,
): BooleanValue {
    return BooleanValue(!args[0].value.isTruthy())
}

fun builtinDisplay(
    args: List<FuncArg>,
    executor: Executor,
): SchemeValue {
    val arg = args[0].value

    if (arg is StringValue) {
        print(arg.toPureString())
    } else {
        print(arg)
    }

    return VoidValue()
}

fun builtinNewline(
    args: List<FuncArg>,
    executor: Executor,
): SchemeValue {
    println()
    return VoidValue()
}

fun builtinCool(
    args: List<FuncArg>,
    executor: Executor,
): SchemeValue {
    println("cool")
    return VoidValue()
}
