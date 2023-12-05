package cardscheme

fun injectBuiltin(environment: Environment) {
    environment.put("+", NativeFuncValue("+", Arity(2, Int.MAX_VALUE), ::builtinPlus))
    environment.put("-", NativeFuncValue("-", Arity(2, Int.MAX_VALUE), ::builtinMinus))
    environment.put("*", NativeFuncValue("*", Arity(2, Int.MAX_VALUE), ::builtinMul))
    environment.put("/", NativeFuncValue("/", Arity(2, Int.MAX_VALUE), ::builtinDiv))
    environment.put("floor-remainder", NativeFuncValue("floor-remainder", Arity(2, 2), ::builtinFloorRemainder))
    environment.put("modulo", NativeFuncValue("modulo", Arity(2, 2), ::builtinFloorRemainder))
    environment.put("abs", NativeFuncValue("abs", Arity(1, 1), ::builtinAbs))
    environment.put("sqrt", NativeFuncValue("sqrt", Arity(1, 1), ::builtinSqrt))

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
    environment.put("cons", NativeFuncValue("cons", Arity(2, 2), ::builtInCons))
    environment.put("append", NativeFuncValue("append", Arity(0, Int.MAX_VALUE), ::builtInAppend))
    environment.put("length", NativeFuncValue("length", Arity(1, 1), ::builtInLength))

    environment.put("vector", NativeFuncValue("vector", Arity(1, Int.MAX_VALUE), ::builtinVector))
    environment.put("vector?", NativeFuncValue("vector?", Arity(1, 1), ::builtinIsVector))
    environment.put("make-vector", NativeFuncValue("make-vector", Arity(1, 2), ::builtinMakeVector))
    environment.put("vector-length", NativeFuncValue("vector-length", Arity(1, 1), ::builtinVectorLength))
    environment.put("vector-ref", NativeFuncValue("vector-ref", Arity(2, 2), ::builtinVectorRef))
    environment.put("vector-set!", NativeFuncValue("vector-set!", Arity(3, 3), ::builtinVectorSet))

    environment.put("string-append", NativeFuncValue("string-append", Arity(0, Int.MAX_VALUE), ::builtinStringAppend))
    environment.put("string->number", NativeFuncValue("string->number", Arity(1, 1), ::builtinStringToNumber))
    environment.put("number->string", NativeFuncValue("number->string", Arity(1, 1), ::builtinNumberToString))
    environment.put("string-length", NativeFuncValue("string-length", Arity(1, 1), ::builtinStringLength))
    environment.put("make-string", NativeFuncValue("make-string", Arity(1, 2), ::builtinMakeString))

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

/**
 * Builtin absolut function.
 *
 * The abs procedure returns the absolute value of its argument.
 *
 * Spec: R7R, chapter 6.2.6
 * Syntax: (abs x)
 */
fun builtinAbs(
    args: List<FuncArg>,
    executor: Executor,
): NumberValue {
    val n = verifyType<NumberValue>(args.first(), "Only numbers can have an absolut")
    return n.abs()
}

/**
 * Builtin sqrt function.
 *
 * Returns the principal square root of z
 *
 * Spec: R7R, chapter 6.2.6
 * Syntax: (sqrt z)
 *
 * NOTE: We intentionally deviate from the standard here and consider it an error calling the function with negative
 * numbers. This is due to the lack of a native complex number implementation.
 */
fun builtinSqrt(
    args: List<FuncArg>,
    executor: Executor,
): NumberValue {
    val n = verifyType<NumberValue>(args.first(), "Only numbers can have an square roots")
    if (n.smallerThan(IntegerValue(0)).value) {
        throw SchemeError(
            "Invalid argument",
            "You can only calculate the square route of positive numbers in cardscheme, but the value was $n.",
            args.first().location,
            null,
        )
    }
    return n.sqrt()
}

fun builtinFloorRemainder(
    args: List<FuncArg>,
    executor: Executor,
): IntegerValue {
    val arguments = verifyAllType<IntegerValue>(args, "Only integers are allowed for modulo")
    return IntegerValue(arguments[0].value % arguments[1].value)
}

fun builtinList(
    args: List<FuncArg>,
    executor: Executor,
): SchemeValue {
    return ListValue(SchemeList(args.map { a -> a.value }))
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

    return ListValue(SchemeList(list.values.tail()))
}

/**
 * Built in string length
 *
 * Spec: R7R, Chapter 6.7
 * Syntax: (string-length string)
 * */
fun builtinStringLength(
    args: List<FuncArg>,
    executor: Executor,
): IntegerValue {
    val arg = verifyType<StringValue>(args.first(), "I expected a string here")
    return IntegerValue(arg.value.length)
}

/**
 * Built in make string
 *
 * Spec: R7R, Chapter 6.7
 * Syntax: (make-string k)
 *         (make-string k char )
 * */
fun builtinMakeString(
    args: List<FuncArg>,
    executor: Executor,
): StringValue {
    val count = verifyType<IntegerValue>(args.first(), "I expected a integer here")
    var character: CharacterValue = CharacterValue('\n')
    if (args.size > 1) {
        character = verifyType<CharacterValue>(args[1], "I expected a character here")
    }
    return StringValue(character.value.toString().repeat(count.value))
}

/**
 * Built in cons (adds to the front of the pair)
 *
 * Spec: R7R, Chapter 6.4
 * Syntax: (cons obj1 obj2)
 * */
fun builtInCons(
    args: List<FuncArg>,
    executor: Executor,
): SchemeValue {
    if (args[1].value is ListValue) {
        val list = SchemeList((args[1].value as ListValue).values)
        list.addFirst(args[0].value)
        return ListValue(list)
    }

    val list = SchemeList(listOf(args[0].value, args[1].value))
    return ListValue(list)
}

/**
 * Built in append for lists
 *
 * Spec: R7R, Chapter 6.4
 * Syntax: (append list ...)
 * */
fun builtInAppend(
    args: List<FuncArg>,
    executor: Executor,
): ListValue {
    if (args.isEmpty()) {
        return ListValue(SchemeList())
    }
    val allArgsButLast = verifyAllType<ListValue>(args.dropLast(1), "Expected this to be a list")
    val newList = SchemeList<SchemeValue>()

    for (arg in allArgsButLast) {
        newList.addAll(arg.values)
    }

    if (args.last().value is ListValue) {
        newList.addAll((args.last().value as ListValue).values)
    } else {
        newList.add(args.last().value)
    }

    return ListValue(newList)
}

/**
 * Built in length for lists
 *
 * Spec: R7R, Chapter 6.4
 * Syntax: (length list)
 * */
fun builtInLength(
    args: List<FuncArg>,
    executor: Executor,
): IntegerValue {
    val arg = verifyType<ListValue>(args.first(), "Expected a list here")
    return IntegerValue(arg.values.size)
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

    if (!func.arity.inside(lists.size)) {
        val message =
            if (func.arity.min == func.arity.max) {
                "The procedure provided expects ${func.arity.min} arguments but provided ${lists.size} lists."
            } else {
                "The procedure provided expects between ${func.arity.min} and ${func.arity.min} arguments but provided ${lists.size} lists."
            }
        throw SchemeError("Invalid number of arguments", message, null, null)
    }

    val values = SchemeList<SchemeValue>()
    val iterators = lists.map { l -> l.values.iterator() }
    while (iterators.all { i -> i.hasNext() }) {
        val iterationArgs = iterators.map { i -> FuncArg(i.next(), null) }
        values.add(executor.callFunction(func, iterationArgs))
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
    val k =
        verifyType<IntegerValue>(args.first(), "Only positive integers can be used to specify the length of a vector")
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
