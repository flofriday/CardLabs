package cardscheme

fun injectBuiltin(environment: Environment) {
    environment.put("+", NativeFuncValue("+", Arity(2, Int.MAX_VALUE, false), ::builtinPlus))
    environment.put("-", NativeFuncValue("-", Arity(2, Int.MAX_VALUE, false), ::builtinMinus))
    environment.put("*", NativeFuncValue("*", Arity(2, Int.MAX_VALUE, false), ::builtinMul))
    environment.put("/", NativeFuncValue("/", Arity(2, Int.MAX_VALUE, false), ::builtinDiv))
    environment.put("floor-remainder", NativeFuncValue("floor-remainder", Arity(2, 2, false), ::builtinFloorRemainder))
    environment.put("modulo", NativeFuncValue("modulo", Arity(2, 2, false), ::builtinFloorRemainder))
    environment.put("abs", NativeFuncValue("abs", Arity(1, 1, false), ::builtinAbs))
    environment.put("sqrt", NativeFuncValue("sqrt", Arity(1, 1, false), ::builtinSqrt))
    environment.put("max", NativeFuncValue("max", Arity(1, Int.MAX_VALUE, false), ::builtinMax))
    environment.put("min", NativeFuncValue("min", Arity(1, Int.MAX_VALUE, false), ::builtinMin))

    environment.put("zero?", NativeFuncValue("zero?", Arity(1, 1, false), ::builtinIsZero))
    environment.put("positive?", NativeFuncValue("positive?", Arity(1, 1, false), ::builtinIsPositive))
    environment.put("negative?", NativeFuncValue("negative?", Arity(1, 1, false), ::builtinIsNegative))
    environment.put("odd?", NativeFuncValue("odd?", Arity(1, 1, false), ::builtinIsOdd))
    environment.put("even?", NativeFuncValue("even?", Arity(1, 1, false), ::builtinIsEven))
    environment.put("=", NativeFuncValue("=", Arity(2, Int.MAX_VALUE, false), ::builtinNumericalEqual))
    environment.put("<", NativeFuncValue("<", Arity(2, Int.MAX_VALUE, false), ::builtinSmaller))
    environment.put("<=", NativeFuncValue("<=", Arity(2, Int.MAX_VALUE, false), ::builtinSmallerEqual))
    environment.put(">", NativeFuncValue(">", Arity(2, Int.MAX_VALUE, false), ::builtinGreater))
    environment.put(">=", NativeFuncValue(">=", Arity(2, Int.MAX_VALUE, false), ::builtinGreaterEqual))

    environment.put("equal?", NativeFuncValue("equal?", Arity(2, Int.MAX_VALUE, false), ::builtinIsEqual))

    environment.put("and", NativeFuncValue("and", Arity(0, Int.MAX_VALUE, false), ::builtinAnd))
    environment.put("or", NativeFuncValue("or", Arity(0, Int.MAX_VALUE, false), ::builtinOr))
    environment.put("not", NativeFuncValue("not", Arity(1, 1, false), ::builtinNot))

    environment.put("pair?", NativeFuncValue("pair?", Arity(1, 1, false), ::builtinIsPair))
    environment.put("null?", NativeFuncValue("null?", Arity(1, 1, false), ::builtinIsNull))
    environment.put("list", NativeFuncValue("list", Arity(0, Int.MAX_VALUE, false), ::builtinList))
    environment.put("car", NativeFuncValue("car", Arity(1, 1, false), ::builtinCar))
    environment.put("cdr", NativeFuncValue("cdr", Arity(1, 1, false), ::builtinCdr))
    environment.put("set-car!", NativeFuncValue("set-car!", Arity(2, 2, false), ::builtinSetCar))
    environment.put("set-cdr!", NativeFuncValue("set-cdr!", Arity(2, 2, false), ::builtinSetCdr))
    environment.put("cons", NativeFuncValue("cons", Arity(2, 2, false), ::builtInCons))
    environment.put("append", NativeFuncValue("append", Arity(0, Int.MAX_VALUE, false), ::builtInAppend))
    environment.put("length", NativeFuncValue("length", Arity(1, 1, false), ::builtInLength))
    environment.put("reverse", NativeFuncValue("reverse", Arity(1, 1, false), ::builtInReverse))

    environment.put("apply", NativeFuncValue("apply", Arity(2, Int.MAX_VALUE, false), ::builtinApply))
    environment.put("map", NativeFuncValue("map", Arity(2, Int.MAX_VALUE, false), ::builtinMap))

    environment.put("vector", NativeFuncValue("vector", Arity(1, Int.MAX_VALUE, false), ::builtinVector))
    environment.put("vector?", NativeFuncValue("vector?", Arity(1, 1, false), ::builtinIsVector))
    environment.put("make-vector", NativeFuncValue("make-vector", Arity(1, 2, false), ::builtinMakeVector))
    environment.put("vector-length", NativeFuncValue("vector-length", Arity(1, 1, false), ::builtinVectorLength))
    environment.put("vector-ref", NativeFuncValue("vector-ref", Arity(2, 2, false), ::builtinVectorRef))
    environment.put("vector-set!", NativeFuncValue("vector-set!", Arity(3, 3, false), ::builtinVectorSet))

    environment.put("string?", NativeFuncValue("string?", Arity(1, 1, false), ::builtinIsString))
    environment.put(
        "string-append",
        NativeFuncValue("string-append", Arity(0, Int.MAX_VALUE, false), ::builtinStringAppend),
    )
    environment.put("string->number", NativeFuncValue("string->number", Arity(1, 1, false), ::builtinStringToNumber))
    environment.put("number->string", NativeFuncValue("number->string", Arity(1, 1, false), ::builtinNumberToString))
    environment.put("symbol->string", NativeFuncValue("symbol->string", Arity(1, 1, false), ::builtinSymbolToString))
    environment.put("string->symbol", NativeFuncValue("string->symbol", Arity(1, 1, false), ::builtinStringToSymbol))
    environment.put("string-length", NativeFuncValue("string-length", Arity(1, 1, false), ::builtinStringLength))
    environment.put("make-string", NativeFuncValue("make-string", Arity(1, 2, false), ::builtinMakeString))

    environment.put("symbol?", NativeFuncValue("symbol?", Arity(1, 1, false), ::builtinIsSymbol))
    environment.put("symbol=?", NativeFuncValue("symbol=?", Arity(2, Int.MAX_VALUE, false), ::builtinSymbolEqual))

    environment.put("display", NativeFuncValue("display", Arity(1, 1, false), ::builtinDisplay))
    environment.put("newline", NativeFuncValue("newline", Arity(0, 0, false), ::builtinNewline))
    environment.put("cool", NativeFuncValue("cool", Arity(0, 0, false), ::builtinCool))
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
 * Spec: R7RS, chapter 6.2.6
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
 * Spec: R7RS, chapter 6.2.6
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
    if (n.smallerThan(IntegerValue(0, executor.schemeSecurityMonitor)).value) {
        throw SchemeError(
            "Invalid argument",
            "You can only calculate the square route of positive numbers in cardscheme, but the value was $n.",
            args.first().location,
            null,
        )
    }
    return n.sqrt()
}

/**
 * Builtin zero? function.
 *
 * Spec: R7RS, chapter 6.2.6
 * Syntax: (zero? n)
 * Semantic: Returns true if x is zero.
 */
fun builtinIsZero(
    args: List<FuncArg>,
    executor: Executor,
): BooleanValue {
    val n = verifyType<NumberValue>(args.first(), "Only numbers can be tested for zero")
    return n.numEqual(IntegerValue(0, executor.schemeSecurityMonitor))
}

/**
 * Builtin positive? function.
 *
 * Spec: R7RS, chapter 6.2.6
 * Syntax: (positive? n)
 * Semantic: Returns true if x is positive, not including zero.
 */
fun builtinIsPositive(
    args: List<FuncArg>,
    executor: Executor,
): BooleanValue {
    val n = verifyType<NumberValue>(args.first(), "Only numbers can be tested for positivity")
    return IntegerValue(0, executor.schemeSecurityMonitor).smallerThan(n)
}

/**
 * Builtin negative? function.
 *
 * Spec: R7RS, chapter 6.2.6
 * Syntax: (negative? x)
 * Semantics: Returns true if x is negative, not including zero
 */
fun builtinIsNegative(
    args: List<FuncArg>,
    executor: Executor,
): BooleanValue {
    val n = verifyType<NumberValue>(args.first(), "Only numbers can be tested for negativity")
    return n.smallerThan(IntegerValue(0, executor.schemeSecurityMonitor))
}

/**
 * Builtin even? function.
 *
 * Spec: R7RS, chapter 6.2.6
 * Syntax: (even? n)
 * Semantics: Returns true if n is even. n must be an integer.
 */
fun builtinIsEven(
    args: List<FuncArg>,
    executor: Executor,
): BooleanValue {
    val n = verifyType<IntegerValue>(args.first(), "Only integers can be tested for even")
    return BooleanValue(n.value % 2 == 0, executor.schemeSecurityMonitor)
}

/**
 * Builtin odd? function.
 *
 * Spec: R7RS, chapter 6.2.6
 * Syntax: (XX n)
 * Semantics: Returns true if n is odd. n must be an integer.
 */
fun builtinIsOdd(
    args: List<FuncArg>,
    executor: Executor,
): BooleanValue {
    val n = verifyType<IntegerValue>(args.first(), "Only integers can be tested for odd")
    return BooleanValue(n.value % 2 != 0, executor.schemeSecurityMonitor)
}

/**
 * Builtin max function.
 *
 * Spec: R7RS, chapter 6.2.6
 * Syntax: (max arg1 arg2...)
 * Semantics: Returns the maximum of their arguments
 */
fun builtinMax(
    args: List<FuncArg>,
    executor: Executor,
): NumberValue {
    val numbers = verifyAllType<NumberValue>(args, "Only numbers are allowed for max")
    return numbers.reduce { acc, n -> if (n.smallerThan(acc).value) acc else n }
}

/**
 * Builtin min function.
 *
 * Spec: R7RS, chapter 6.2.6
 * Syntax: (min arg1 arg2...)
 * Semantics: Returns the minimum of their arguments
 */
fun builtinMin(
    args: List<FuncArg>,
    executor: Executor,
): NumberValue {
    val numbers = verifyAllType<NumberValue>(args, "Only numbers are allowed for min")
    return numbers.reduce { acc, n -> if (acc.smallerThan(n).value) acc else n }
}

fun builtinFloorRemainder(
    args: List<FuncArg>,
    executor: Executor,
): IntegerValue {
    val arguments = verifyAllType<IntegerValue>(args, "Only integers are allowed for modulo")
    return IntegerValue(arguments[0].value % arguments[1].value, executor.schemeSecurityMonitor)
}

/**
 * Checks if the object is a non-empty list.
 *
 * Spec: R7RS, chapter 6.4
 * Syntax: (pair? obj)
 * Semantic: Returns #t if obj is a non-empty list, otherwise returns #f.
 */
fun builtinIsPair(
    args: List<FuncArg>,
    executor: Executor,
): BooleanValue {
    val obj = args.first().value
    if (obj !is ListValue) {
        return BooleanValue(false, executor.schemeSecurityMonitor)
    }

    return BooleanValue(!obj.values.isEmpty(), executor.schemeSecurityMonitor)
}

/**
 * Checks if the object is an empty list.
 *
 * Spec: R7RS, chapter 6.4
 * Syntax: (null? obj)
 * Semantic: Returns #t if obj is the empty list, otherwise returns #f.
 */
fun builtinIsNull(
    args: List<FuncArg>,
    executor: Executor,
): BooleanValue {
    val obj = args.first().value
    if (obj !is ListValue) {
        return BooleanValue(false, executor.schemeSecurityMonitor)
    }

    return BooleanValue(obj.values.isEmpty(), executor.schemeSecurityMonitor)
}

fun builtinList(
    args: List<FuncArg>,
    executor: Executor,
): SchemeValue {
    return ListValue(args.map { a -> a.value }, executor.schemeSecurityMonitor)
}

fun builtinCar(
    args: List<FuncArg>,
    executor: Executor,
): SchemeValue {
    val list = verifyType<ListValue>(args.first(), "I expected a list here")
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

    return ListValue(list.values.tail(), executor.schemeSecurityMonitor)
}

/**
 * Builtin set-car! function.
 *
 * Spec: R7RS, chapter 6.4
 * Syntax: (set-car! pair obj)
 * Semantics: Stores obj in the car field of pair.
 */
fun builtinSetCar(
    args: List<FuncArg>,
    executor: Executor,
): SchemeValue {
    val list = verifyType<ListValue>(args.first(), "I expected a list here")
    if (list.values.isEmpty()) {
        throw SchemeError("Invalid Argument", "The list to `set-car!` cannot be empty", args.first().location, null)
    }
    val obj = args[1].value
    list.values.setHead(obj)
    return VoidValue(executor.schemeSecurityMonitor)
}

/**
 * Builtin set-cdr! function.
 *
 * Spec: R7RS, chapter 6.4
 * Syntax: (set-cdr! pair obj)
 * Semantics: Stores obj in the cdr field of pair.
 */
fun builtinSetCdr(
    args: List<FuncArg>,
    executor: Executor,
): SchemeValue {
    val list = verifyType<ListValue>(args.first(), "I expected a list here")
    if (list.values.isEmpty()) {
        throw SchemeError("Invalid Argument", "The list to `set-cdr!` cannot be empty", args.first().location, null)
    }
    val obj = args[1].value
    val objList = if (obj is ListValue) obj else ListValue(executor.schemeSecurityMonitor, obj)
    list.values.setTail(objList.values)
    return VoidValue(executor.schemeSecurityMonitor)
}

/**
 * Built in string length
 *
 * Spec: R7RS, Chapter 6.7
 * Syntax: (string-length string)
 * */
fun builtinStringLength(
    args: List<FuncArg>,
    executor: Executor,
): IntegerValue {
    val arg = verifyType<StringValue>(args.first(), "I expected a string here")
    return IntegerValue(arg.value.length, executor.schemeSecurityMonitor)
}

/**
 * Built in make string
 *
 * Spec: R7RS, Chapter 6.7
 * Syntax: (make-string k)
 *         (make-string k char )
 * */
fun builtinMakeString(
    args: List<FuncArg>,
    executor: Executor,
): StringValue {
    val count = verifyType<IntegerValue>(args.first(), "I expected a integer here")
    var character: CharacterValue = CharacterValue('\n', executor.schemeSecurityMonitor)
    if (args.size > 1) {
        character = verifyType<CharacterValue>(args[1], "I expected a character here")
    }
    return StringValue(character.value.toString().repeat(count.value), executor.schemeSecurityMonitor)
}

/**
 * Built in cons (returns a new pair with two elements obj1 as car and ob2 as cdr)
 *
 * Spec: R7RS, Chapter 6.4
 * Syntax: (cons obj1 obj2)
 * Semantic: Returns a newly allocated pair whose car is obj1 and whose cdr is obj2.
 * */
fun builtInCons(
    args: List<FuncArg>,
    executor: Executor,
): SchemeValue {
    if (args[1].value is ListValue) {
        val list = SchemeList((args[1].value as ListValue).values)
        list.addFirst(args[0].value)
        return ListValue(list, executor.schemeSecurityMonitor)
    }

    return ListValue(executor.schemeSecurityMonitor, args[0].value, args[1].value)
}

/**
 * Built in append for lists
 *
 * Spec: R7RS, Chapter 6.4
 * Syntax: (append list ...)
 * */
fun builtInAppend(
    args: List<FuncArg>,
    executor: Executor,
): ListValue {
    if (args.isEmpty()) {
        return ListValue(executor.schemeSecurityMonitor)
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

    return ListValue(newList, executor.schemeSecurityMonitor)
}

/**
 * Built in length for lists
 *
 * Spec: R7RS, Chapter 6.4
 * Syntax: (length list)
 * */
fun builtInLength(
    args: List<FuncArg>,
    executor: Executor,
): IntegerValue {
    val arg = verifyType<ListValue>(args.first(), "Expected a list here")
    return IntegerValue(arg.values.size, executor.schemeSecurityMonitor)
}

/**
 * Built in reverse for lists.
 *
 * Spec: R7RS, Chapter 6.4
 * Syntax: (reverse list)
 * Semantics: Returns a newly allocated list consisting of the elements of list in reverse order.
 */
fun builtInReverse(
    args: List<FuncArg>,
    executor: Executor,
): ListValue {
    val arg = verifyType<ListValue>(args.first(), "Expected a list here")
    return ListValue(arg.values.reversed(), executor.schemeSecurityMonitor)
}

/**
 * Built in apply function.
 *
 * Spec: R7RS, Chapter 6.10
 * Syntax: (apply proc arg1 ... args)
 * Semantics: The apply procedure calls proc with the elements of the list (append (list arg1 ...) args) as the
 * actual arguments.
 */
fun builtinApply(
    args: List<FuncArg>,
    executor: Executor,
): SchemeValue {
    val func = verifyType<CallableValue>(args.first(), "I expected a function here")
    val arguments = args.drop(1).dropLast(1).toMutableList()
    val list = verifyType<ListValue>(args.last(), "I expected this to be a list")
    arguments += list.values.map { v -> FuncArg(v, null) }

    return executor.callFunction(func, arguments)
}

/**
 * Built in map function.
 *
 * Spec: R7RS, Chapter 6.10
 * Syntax: (map proc list1 list2 ...)
 * */
fun builtinMap(
    args: List<FuncArg>,
    executor: Executor,
): ListValue {
    val func = verifyType<CallableValue>(args.first(), "The first argument must be a procedure")
    val lists = verifyAllType<ListValue>(args.drop(1), "All arguments after the first one must be lists")

    // Custom error, for better readability
    if (!func.arity.inside(lists.size)) {
        val message =
            if (func.arity.min == func.arity.max) {
                "The procedure provided expects ${func.arity.min} arguments but provided ${lists.size} lists."
            } else {
                "The procedure provided expects between ${func.arity.min} and ${func.arity.min} arguments but provided ${lists.size} lists."
            }
        throw SchemeError("Invalid number of arguments", message, null, null)
    }

    val values = mutableListOf<SchemeValue>()
    val iterators = lists.map { l -> l.values.iterator() }
    while (iterators.all { i -> i.hasNext() }) {
        val iterationArgs = iterators.map { i -> FuncArg(i.next(), null) }
        values.add(executor.callFunction(func, iterationArgs))
    }
    return ListValue(values, executor.schemeSecurityMonitor)
}

fun builtinVector(
    args: List<FuncArg>,
    executor: Executor,
): VectorValue {
    return VectorValue(args.map { a -> a.value }.toMutableList(), executor.schemeSecurityMonitor)
}

fun builtinIsVector(
    args: List<FuncArg>,
    executor: Executor,
): BooleanValue {
    return BooleanValue(args.first().value is VectorValue, executor.schemeSecurityMonitor)
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
            VoidValue(executor.schemeSecurityMonitor)
        }
    return VectorValue((1..k.value).map { filler }.toMutableList(), executor.schemeSecurityMonitor)
}

fun builtinVectorLength(
    args: List<FuncArg>,
    executor: Executor,
): IntegerValue {
    val vec = verifyType<VectorValue>(args.first(), "Only vectors are expected here")
    return IntegerValue(vec.values.size, executor.schemeSecurityMonitor)
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

    return VoidValue(executor.schemeSecurityMonitor)
}

fun builtinNumericalEqual(
    args: List<FuncArg>,
    executor: Executor,
): BooleanValue {
    try {
        val numbers = verifyAllType<NumberValue>(args, "Only numbers can be compared with `=`")
        val result = numbers.zipWithNext { a, b -> a.numEqual(b) }.all { b -> b.value }
        return BooleanValue(result, executor.schemeSecurityMonitor)
    } catch (e: SchemeError) {
        throw SchemeError(e.header, e.reason, e.location, e.tip ?: "You can use `equal?` to compare other types.")
    }
}

fun builtinIsEqual(
    args: List<FuncArg>,
    executor: Executor,
): BooleanValue {
    val result = args.map { a -> a.value }.zipWithNext { a, b -> a == b }.all { it }
    return BooleanValue(result, executor.schemeSecurityMonitor)
}

/**
 * Built in is string
 *
 * Spec: R7RS, Chapter 6.7
 * Syntax: (string? obj)
 * Semantics: Returns #t if obj is a string, otherwise returns #f.
 * */
fun builtinIsString(
    args: List<FuncArg>,
    executor: Executor,
): BooleanValue {
    return BooleanValue(args.first().value is StringValue, executor.schemeSecurityMonitor)
}

/**
 * Built in string append
 *
 * Spec: R7RS, Chapter 6.7
 * Syntax: (string-append string ...)
 * */
fun builtinStringAppend(
    args: List<FuncArg>,
    executor: Executor,
): StringValue {
    verifyAllType<StringValue>(args, "Only strings can be appended")
    return StringValue(args.joinToString("") { a -> (a.value as StringValue).value }, executor.schemeSecurityMonitor)
}

/**
 * Built in number to string conversion
 *
 * Spec: R7RS, Chapter 6.2.7
 * Syntax: (number->string z)
 *         (number->string z radix ) // FIXME: Not implemented
 * */
fun builtinNumberToString(
    args: List<FuncArg>,
    executor: Executor,
): StringValue {
    verifyType<NumberValue>(args[0], "Only numbers can be converted to string by this function")
    return StringValue(args[0].value.toString(), executor.schemeSecurityMonitor)
}

/**
 * Built in string to number conversion
 *
 * Spec: R7RS, Chapter 6.2.7
 * Syntax: (string->number string)
 *         (string->number string radix) // FIXME: Not implemented
 * */
fun builtinStringToNumber(
    args: List<FuncArg>,
    executor: Executor,
): SchemeValue {
    val stringValue =
        verifyType<StringValue>(args[0], "Only strings can be converted to numbers by this function").value
    if (stringValue.contains(".")) {
        return FloatValue(stringValue.toFloat(), executor.schemeSecurityMonitor)
    }

    return IntegerValue(stringValue.toInt(), executor.schemeSecurityMonitor)
}

/**
 * Built in symbol to string conversion
 *
 * Spec: R7RS, Chapter 6.5
 * Syntax: (symbol->string symbol)
 * Semantics: Returns the name of symbol as a string, but without adding escapes.
 * */
fun builtinSymbolToString(
    args: List<FuncArg>,
    executor: Executor,
): StringValue {
    verifyType<SymbolValue>(args[0], "Only symbols can be converted to string by this function")
    return StringValue(args[0].value.toString(), executor.schemeSecurityMonitor)
}

/**
 * Built in string to symbol conversion
 *
 * Spec: R7RS, Chapter 6.5
 * Syntax: (string->symbol string)
 * Semantics: Returns the symbol whose name is string. This procedure can create symbols with names containing
 * special characters that would require escaping when written, but does not interpret escapes in its input.
 */
fun builtinStringToSymbol(
    args: List<FuncArg>,
    executor: Executor,
): SymbolValue {
    val string = verifyType<StringValue>(args[0], "Only strings can be converted to symbols by this function")
    return SymbolValue(string.value, executor.schemeSecurityMonitor)
}

/**
 * Built in check for symbol
 *
 * Spec: R7RS, Chapter 6.5
 * Syntax: (symbol? obj)
 * Semantics: Returns #t if obj is a symbol, otherwise returns #f.
 */
fun builtinIsSymbol(
    args: List<FuncArg>,
    executor: Executor,
): BooleanValue {
    return BooleanValue(args.first().value is SymbolValue, executor.schemeSecurityMonitor)
}

/**
 * Built in equality for symbols
 *
 * Spec: R7RS, Chapter 6.5
 * Syntax: (symbol=? symbol1 symbol2 symbol3 ...)
 * Semantics: Returns #t if all the arguments are symbols and all have the same names in the sense of string=?.
 */
fun builtinSymbolEqual(
    args: List<FuncArg>,
    executor: Executor,
): BooleanValue {
    val symbols = verifyAllType<SymbolValue>(args, "I expected all arguments to be symbols")
    return BooleanValue(symbols.zipWithNext { a, b -> a.value == b.value }.all { t -> t }, executor.schemeSecurityMonitor)
}

fun builtinSmallerEqual(
    args: List<FuncArg>,
    executor: Executor,
): BooleanValue {
    verifyAllType<NumberValue>(args, "Only numbers can be compared")

    val result =
        args
            .map { a -> a.value as NumberValue }
            .zipWithNext { a, b -> a.smallerThan(b).value || a.numEqual(b).value }
            .all { it }
    return BooleanValue(result, executor.schemeSecurityMonitor)
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
    return BooleanValue(result, executor.schemeSecurityMonitor)
}

fun builtinGreater(
    args: List<FuncArg>,
    executor: Executor,
): BooleanValue {
    verifyAllType<NumberValue>(args, "Only numbers can be compared")

    val result =
        args
            .map { a -> a.value as NumberValue }
            .zipWithNext { a, b -> a.smallerThan(b).value || a.numEqual(b).value }
            .none { it }
    return BooleanValue(result, executor.schemeSecurityMonitor)
}

fun builtinGreaterEqual(
    args: List<FuncArg>,
    executor: Executor,
): BooleanValue {
    verifyAllType<NumberValue>(args, "Only numbers can be compared")

    val result =
        args
            .map { a -> a.value as NumberValue }
            .zipWithNext { a, b -> !a.smallerThan(b).value || a.numEqual(b).value }
            .all { it }
    return BooleanValue(result, executor.schemeSecurityMonitor)
}

/**
 * Built in logical and
 *
 * Spec: R7RS, Chapter 4.2.1
 * */
fun builtinAnd(
    args: List<FuncArg>,
    executor: Executor,
): SchemeValue {
    if (args.isEmpty()) {
        return BooleanValue(true, executor.schemeSecurityMonitor)
    }
    for (arg in args) {
        if (!arg.value.isTruthy()) {
            return BooleanValue(false, executor.schemeSecurityMonitor)
        }
    }
    return args.last().value
}

/**
 * Built in logical or
 *
 * Spec: R7RS, Chapter 4.2.1
 * */
fun builtinOr(
    args: List<FuncArg>,
    executor: Executor,
): SchemeValue {
    if (args.isEmpty()) {
        return BooleanValue(false, executor.schemeSecurityMonitor)
    }
    for (arg in args) {
        if (arg.value.isTruthy()) {
            return arg.value
        }
    }
    return BooleanValue(false, executor.schemeSecurityMonitor)
}

/**
 * Built in logical not
 *
 * Spec: R7RS, Chapter 6.3
 * */
fun builtinNot(
    args: List<FuncArg>,
    executor: Executor,
): BooleanValue {
    return BooleanValue(!args[0].value.isTruthy(), executor.schemeSecurityMonitor)
}

fun builtinDisplay(
    args: List<FuncArg>,
    executor: Executor,
): SchemeValue {
    val arg = args[0].value

    if (arg is StringValue) {
        executor.printToBuffer(arg.toPureString())
    } else {
        executor.printToBuffer(arg)
    }

    return VoidValue(executor.schemeSecurityMonitor)
}

fun builtinNewline(
    args: List<FuncArg>,
    executor: Executor,
): SchemeValue {
    executor.printToBuffer("\n")
    return VoidValue(executor.schemeSecurityMonitor)
}

fun builtinCool(
    args: List<FuncArg>,
    executor: Executor,
): SchemeValue {
    executor.printToBuffer("cool\n")
    return VoidValue(executor.schemeSecurityMonitor)
}
