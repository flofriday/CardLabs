fun injectBuiltin(environment: Environment) {
    environment.put("+", NativeFunc("+", ::builtinPlus))
    environment.put("-", NativeFunc("-", ::builtinMinus))
    environment.put("cool", NativeFunc("cool", ::builtinCool))
}

fun builtinPlus(args: List<SchemeValue>, env: Environment): SchemeValue {
    // FIXME: define value addition better
    var sum = 0
    for (arg in args) {
        if (arg !is IntValue) throw Exception("Only Ints can be added for now")
        sum += (arg as IntValue).value
    }
    return IntValue(sum)
}

fun builtinMinus(args: List<SchemeValue>, env: Environment): SchemeValue {
    // FIXME: define value subtraction better
    if (args.first() !is IntValue) throw Exception("Only Ints can be subtracted for now")
    var sum = (args.first() as IntValue).value

    for (arg in args.drop(1)) {
        if (arg !is IntValue) throw Exception("Only Ints can be subtracted for now")
        sum -= (arg as IntValue).value
    }
    return IntValue(sum)
}

fun builtinCool(args: List<SchemeValue>, env: Environment): SchemeValue {
    // FIXME: Return something better than -1
    println("cool")
    return IntValue(-1)
}
