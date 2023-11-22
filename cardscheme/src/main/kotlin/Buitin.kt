fun injectBuiltin(environment: Environment) {
    environment.put("+", NativeFuncValue("+", ::builtinPlus))
    environment.put("-", NativeFuncValue("-", ::builtinMinus))
    environment.put("=", NativeFuncValue("=", ::builtinEqual))
    environment.put("<", NativeFuncValue("<", ::builtinSmaller))
    environment.put("<=", NativeFuncValue("<=", ::builtinSmallerEqual))
    environment.put(">", NativeFuncValue(">", ::builtinGreater))
    environment.put(">=", NativeFuncValue(">=", ::builtinGreaterEqual))
    environment.put("display", NativeFuncValue("display", ::builtinDisplay))
    environment.put("newline", NativeFuncValue("newline", ::builtinNewline))
    environment.put("cool", NativeFuncValue("cool", ::builtinCool))
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
    if (args.size != 1) {
        throw SchemeError("Too many or too few arguments", "The display function takes exactly one argument", null, null)
    }
    print(args[0].value)
    return VoidValue()
}

fun builtinNewline(
    args: List<NativeFuncArg>,
    env: Environment,
): SchemeValue {
    if (args.size > 0) {
        throw SchemeError("Too many arguments", "The newline function takes exactly one argument", null, null)
    }
    println()
    return VoidValue()
}

fun builtinSmallerEqual(
    args: List<NativeFuncArg>,
    env: Environment,
): BooleanValue {
    if (args.size < 2) {
        throw SchemeError("Too few arguments", "The equals function takes at least two arguments", null, null)
    }

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
    if (args.size < 2) {
        throw SchemeError("Too few arguments", "The equals function takes at least two arguments", null, null)
    }

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
    if (args.size < 2) {
        throw SchemeError("Too few arguments", "The smaller function takes at least two arguments", null, null)
    }

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
    if (args.size < 2) {
        throw SchemeError("Too few arguments", "The greater function takes at least two arguments", null, null)
    }

    for ((value, loc) in args) {
        if (value !is NumberValue) {
            throw SchemeError("Expected number", "You can only compare numbers.", loc, null)
        }
    }

    val result = args.map { a -> a.value as NumberValue }.zipWithNext { a, b -> a.smallerThan(b).value || a == b }.none() { it }
    return BooleanValue(result)
}

fun builtinGreaterEqual(
    args: List<NativeFuncArg>,
    env: Environment,
): BooleanValue {
    if (args.size < 2) {
        throw SchemeError("Too few arguments", "The greater equal function takes at least two arguments", null, null)
    }

    for ((value, loc) in args) {
        if (value !is NumberValue) {
            throw SchemeError("Expected number", "You can only compare numbers.", loc, null)
        }
    }

    val result = args.map { a -> a.value as NumberValue }.zipWithNext { a, b -> !a.smallerThan(b).value || a == b }.all() { it }
    return BooleanValue(result)
}

fun builtinCool(
    args: List<NativeFuncArg>,
    env: Environment,
): SchemeValue {
    println("cool")
    return VoidValue()
}
