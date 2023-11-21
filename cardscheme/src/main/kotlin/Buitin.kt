fun injectBuiltin(environment: Environment) {
    environment.put("+", NativeFuncValue("+", ::builtinPlus))
    environment.put("-", NativeFuncValue("-", ::builtinMinus))
    environment.put("<", NativeFuncValue("<", ::builtinSmaller))
    environment.put("display", NativeFuncValue("display", ::builtinDisplay))
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
    if (args.size > 1) {
        throw SchemeError("Too many arguments", "The display function takes exactly one argument", null, null)
    }
    print(args[0].value)
    return VoidValue()
}

fun builtinSmaller(
    args: List<NativeFuncArg>,
    env: Environment,
): SchemeValue {
    for ((value, loc) in args) {
        if (value !is NumberValue) {
            throw SchemeError("Expected number", "You can only compare numbers.", loc, null)
        }
    }

    val result = args.map { a -> a.value as NumberValue }.zipWithNext { a, b -> a.smallerThan(b) }.all { it.value }
    return BooleanValue(result)
}

fun builtinCool(
    args: List<NativeFuncArg>,
    env: Environment,
): SchemeValue {
    println("cool")
    return VoidValue()
}
