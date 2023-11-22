fun injectBuiltin(environment: Environment) {
    environment.put("+", NativeFuncValue("+", Arity(2, Int.MAX_VALUE), ::builtinPlus))
    environment.put("-", NativeFuncValue("-", Arity(2, Int.MAX_VALUE), ::builtinMinus))
    environment.put("=", NativeFuncValue("=", Arity(2, Int.MAX_VALUE), ::builtinEqual))
    environment.put("<", NativeFuncValue("<", Arity(2, Int.MAX_VALUE), ::builtinSmaller))
    environment.put("<=", NativeFuncValue("<=", Arity(2, Int.MAX_VALUE), ::builtinSmallerEqual))
    environment.put(">", NativeFuncValue(">", Arity(2, Int.MAX_VALUE), ::builtinGreater))
    environment.put(">=", NativeFuncValue(">=", Arity(2, Int.MAX_VALUE), ::builtinGreaterEqual))
    environment.put("display", NativeFuncValue("display", Arity(1, 1), ::builtinDisplay))
    environment.put("newline", NativeFuncValue("newline", Arity(0, 0), ::builtinNewline))
    environment.put("cool", NativeFuncValue("cool", Arity(0, 0), ::builtinCool))
}

fun builtinPlus(
    args: List<NativeFuncArg>,
    env: Environment,
): NumberValue {
    var sum: NumberValue = IntegerValue(0)
    for ((arg, loc) in args) {
        if (arg !is NumberValue) {
            throw SchemeError(
                "Unsupported Type",
                "Only numbers can be added but one of the arguments was a ${arg.typeName()}",
                loc,
                null,
            )
        }
        sum = sum.add(arg)
    }
    return sum
}

fun builtinMinus(
    args: List<NativeFuncArg>,
    env: Environment,
): SchemeValue {
    if (args.first().value !is NumberValue) {
        throw SchemeError(
            "Unsuported Type",
            "Only numbers can be added but one of the arguments was a ${args.first().value.typeName()}",
            args.first().location,
            null,
        )
    }
    var res = (args.first().value as NumberValue)

    for ((arg, loc) in args.drop(1)) {
        if (arg !is NumberValue) {
            throw throw SchemeError(
                "Unsuported Type",
                "Only numbers can be added but one of the arguments was a ${arg.typeName()}",
                loc,
                null,
            )
        }
        res = res.sub(arg)
    }
    return res
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

    val result = args.map { a -> a.value as NumberValue }.zipWithNext { a, b -> a.smallerThan(b).value || a == b }.all { it }
    return BooleanValue(result)
}

fun builtinEqual(
    args: List<NativeFuncArg>,
    env: Environment,
): BooleanValue {
    for ((value, loc) in args) {
        if (value.javaClass != args[0].value::class.java) {
            throw SchemeError("Expected same type", "Expected all arguments to be of the same type", loc, null)
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

    val result = args.map { a -> a.value as NumberValue }.zipWithNext { a, b -> a.smallerThan(b) }.all { it.value }
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

    val result = args.map { a -> a.value as NumberValue }.zipWithNext { a, b -> a.smallerThan(b).value || a == b }.none { it }
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

    val result = args.map { a -> a.value as NumberValue }.zipWithNext { a, b -> !a.smallerThan(b).value || a == b }.all { it }
    return BooleanValue(result)
}

fun builtinCool(
    args: List<NativeFuncArg>,
    env: Environment,
): SchemeValue {
    println("cool")
    return VoidValue()
}
