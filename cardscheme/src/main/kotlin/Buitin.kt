import java.util.*

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

    environment.put("car", NativeFuncValue("car", Arity(1, 1), ::builtinCar))
    environment.put("cdr", NativeFuncValue("cdr", Arity(1, 1), ::builtinCdr))

    environment.put("display", NativeFuncValue("display", Arity(1, 1), ::builtinDisplay))
    environment.put("newline", NativeFuncValue("newline", Arity(0, 0), ::builtinNewline))
    environment.put("cool", NativeFuncValue("cool", Arity(0, 0), ::builtinCool))
}

fun verifyAllNumbers(args: List<NativeFuncArg>) {
    for ((arg, loc) in args) {
        if (arg !is NumberValue) {
            throw SchemeError(
                "Unsupported Type",
                "I expected numbers here but this one was a ${arg.typeName()}",
                loc,
                null,
            )
        }
    }
}

fun builtinPlus(
    args: List<NativeFuncArg>,
    env: Environment,
): NumberValue {
    verifyAllNumbers(args)
    return args.map { a -> a.value as NumberValue }.reduce { sum, n -> sum.add(n) }
}

fun builtinMinus(
    args: List<NativeFuncArg>,
    env: Environment,
): SchemeValue {
    verifyAllNumbers(args)
    return args.map { a -> a.value as NumberValue }.reduce { res, n -> res.sub(n) }
}

fun builtinMul(
    args: List<NativeFuncArg>,
    env: Environment,
): NumberValue {
    verifyAllNumbers(args)
    return args.map { a -> a.value as NumberValue }.reduce { res, n -> res.mul(n) }
}

fun builtinDiv(
    args: List<NativeFuncArg>,
    env: Environment,
): NumberValue {
    verifyAllNumbers(args)
    return args.map { a -> a.value as NumberValue }.reduce { res, n -> res.div(n) }
}

fun builtinCar(
    args: List<NativeFuncArg>,
    env: Environment,
): SchemeValue {
    val list = args.first().value
    if (list !is ListValue) {
        throw SchemeError("Type Mismatch", "I expected a list here but you provided a ${list.typeName()}", args.first().location, null)
    }

    if (list.values.isEmpty()) {
        throw SchemeError("Empty List", "Car can only be called on non-empty lists", args.first().location, null)
    }

    return list.values.first()
}

fun builtinCdr(
    args: List<NativeFuncArg>,
    env: Environment,
): SchemeValue {
    val list = args.first().value
    if (list !is ListValue) {
        throw SchemeError("Type Mismatch", "I expected a list here but you provided a ${list.typeName()}", args.first().location, null)
    }

    if (list.values.isEmpty()) {
        throw SchemeError("Empty List", "Cdr can only be called on non-empty lists", args.first().location, null)
    }

    // FIXME: Is there a better solution?
    return ListValue(LinkedList(list.values.drop(1)))
}

fun builtinDisplay(
    args: List<NativeFuncArg>,
    env: Environment,
): SchemeValue {
    print(args[0].value)
    return VoidValue()
}

fun builtinNewline(
    args: List<NativeFuncArg>,
    env: Environment,
): SchemeValue {
    println()
    return VoidValue()
}

fun builtinSmallerEqual(
    args: List<NativeFuncArg>,
    env: Environment,
): BooleanValue {
    for ((value, loc) in args) {
        if (value !is NumberValue) {
            throw SchemeError("Expected number", "You can only compare numbers.", loc, null)
        }
    }

    val result =
        args
            .map { a -> a.value as NumberValue }
            .zipWithNext { a, b -> a.smallerThan(b).value || a == b }
            .all { it }
    return BooleanValue(result)
}

fun builtinEqual(
    args: List<NativeFuncArg>,
    env: Environment,
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

fun builtinSmaller(
    args: List<NativeFuncArg>,
    env: Environment,
): BooleanValue {
    for ((value, loc) in args) {
        if (value !is NumberValue) {
            throw SchemeError("Expected number", "You can only compare numbers.", loc, null)
        }
    }

    val result =
        args
            .map { a -> a.value as NumberValue }
            .zipWithNext { a, b -> a.smallerThan(b) }
            .all { it.value }
    return BooleanValue(result)
}

fun builtinGreater(
    args: List<NativeFuncArg>,
    env: Environment,
): BooleanValue {
    for ((value, loc) in args) {
        if (value !is NumberValue) {
            throw SchemeError("Expected number", "You can only compare numbers.", loc, null)
        }
    }

    val result =
        args
            .map { a -> a.value as NumberValue }
            .zipWithNext { a, b -> a.smallerThan(b).value || a == b }
            .none { it }
    return BooleanValue(result)
}

fun builtinGreaterEqual(
    args: List<NativeFuncArg>,
    env: Environment,
): BooleanValue {
    for ((value, loc) in args) {
        if (value !is NumberValue) {
            throw SchemeError("Expected number", "You can only compare numbers.", loc, null)
        }
    }

    val result =
        args
            .map { a -> a.value as NumberValue }
            .zipWithNext { a, b -> !a.smallerThan(b).value || a == b }
            .all { it }
    return BooleanValue(result)
}

fun builtinCool(
    args: List<NativeFuncArg>,
    env: Environment,
): SchemeValue {
    println("cool")
    return VoidValue()
}
