package simulation

import cardscheme.*
import kotlin.random.Random

/**
 * Injects function not in the scheme spec but which are useful to write bots.
 */
fun injectSimulationBuiltin(environment: Environment) {
    // Matching Logic
    environment.put("matches-card?", NativeFuncValue("matches-card?", Arity(2, 2), ::builtinMatchesCard))
    environment.put("matching-cards", NativeFuncValue("matching-cards", Arity(2, 2), ::builtinMatchingCards))

    // Random functions
    environment.put("random", NativeFuncValue("random", Arity(0, 0), ::builtinRandom))
    environment.put("random-choice", NativeFuncValue("random-choice", Arity(1, 1), ::builtinRandomChoice))
}

fun verifyCard(arg: FuncArg): Card {
    try {
        return decodeCard(arg.value)
    } catch (e: DecodeError) {
        throw SchemeError("Type Mismatch", e.reason, arg.location, null)
    }
}

fun verifyCardList(arg: FuncArg): List<Card> {
    val list = verifyType<ListValue>(arg, "I expected a list of cards here")
    return list.values.map { v -> verifyCard(FuncArg(v, null)) }
}

/**
 * Is it possible that second card follows the second?
 */
fun builtinMatchesCard(
    args: List<FuncArg>,
    executor: Executor,
): BooleanValue {
    val first = verifyCard(args[0])
    val second = verifyCard(args[1])

    return BooleanValue(first.match(second))
}

/**
 * Returns  the list of all cards that match the first card.
 */
fun builtinMatchingCards(
    args: List<FuncArg>,
    executor: Executor,
): ListValue {
    val first = verifyCard(args[0])
    val second = verifyCardList(args[1])

    val matchingCards = second.filter { c -> first.match(c) }
        .map { c -> encodeCard(c) }
    return ListValue(SchemeList(matchingCards))
}

/**
 * Generate a random number between 0 and 1.
 */
fun builtinRandom(
    args: List<FuncArg>,
    executor: Executor,
): FloatValue {
    return FloatValue(Random.nextFloat())
}

/**
 * Returns a random element from a list or vector
 */
fun builtinRandomChoice(
    args: List<FuncArg>,
    executor: Executor,
): SchemeValue {
    val collection = args.first().value
    return when (collection) {
        is VectorValue -> collection.values.random()
        is ListValue -> collection.values.random()
        else -> { throw SchemeError("Type Mismatch", "I can only take a random element from vectors or lists, but you provided a ${collection.typeName()}", args.first().location, null) }
    }
}
