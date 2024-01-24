package simulation

import cardscheme.*
import org.slf4j.LoggerFactory
import simulation.models.*
import java.time.Instant
import kotlin.math.abs

fun <E> MutableList<E>.removeFirstN(n: Int): List<E> {
    val elements = mutableListOf<E>()
    for (i in 1..n) {
        elements.addLast(this.removeFirst())
    }
    return elements
}

data class GameState(
    var pile: MutableList<Card>,
    var drawPile: MutableList<Card>,
    var players: List<Player>,
    var currentPlayer: Int = 0,
    var direction: Int = 1,
    var turns: MutableList<Turn> = mutableListOf<Turn>(),
)

private val logger = LoggerFactory.getLogger("Simulation")

fun runSimulation(request: SimulationRequest): SimulationResult {
    val startTime = Instant.now()

    val state = initialGameState(generateDeck().shuffled().toMutableList(), request)
    state.players = state.players.shuffled()
    try {
        initializeBots(state.players)
        while (true) {
            val player = state.players[state.currentPlayer]
            runTurn(state)
            if (player.hand.isEmpty()) {
                logSystem(state.turns.last(), "Bot ${player.bot.name} won!")
                break
            }
        }
        return SimulationResult(
            gameId = request.gameId,
            startTime = startTime,
            endTime = Instant.now(),
            winningBotId = state.players.first { p -> p.hand.isEmpty() }.bot.botId,
            disqualifiedBotId = null,
            turns = state.turns,
            participatingBotIds = state.players.map { p -> p.bot.botId },
        )
    } catch (e: DisqualificationError) {
        if (state.turns.isEmpty()) {
            state.turns.addLast(turnSnapShot(state))
        }
        val turn = state.turns.last()
        val bot = state.players.first { p -> p.bot.botId == e.botId }.bot

        logSystem(turn, "Bot ${bot.name} disqualified!")
        var crashMessage = e.reason
        if (e.schemeError != null) {
            crashMessage += "\n\n" + e.schemeError.format(bot.code, color = false)
        }
        logBot(turn, bot, crashMessage)
        return SimulationResult(
            gameId = request.gameId,
            startTime = startTime,
            endTime = Instant.now(),
            winningBotId = null,
            disqualifiedBotId = e.botId,
            turns = state.turns,
            participatingBotIds = state.players.map { p -> p.bot.botId },
        )
    }
}

/**
 * Executes on turn of one player.
 *
 * Modifies the gamestate to add the turn and sets calculates the next player for the next move.
 */
fun runTurn(state: GameState) {
    val player = state.players[state.currentPlayer]
    val turn = turnSnapShot(state)
    state.turns.addLast(turn)

    // Pick a card if there is no matching one
    if (player.hand.none { card -> state.pile.last().match(card) }) {
        logSystem(turn, "Bot ${player.bot.name} has no matching card and draws one.")
        val pickedCards = pickupCards(state, player, 1)
        state.players
            .filter { p -> p != player }
            .forEach { p ->
                execBotEvent(
                    state,
                    p,
                    "card-picked",
                    listOf(encodeCard(state.pile.last()), encodePlayer(player)),
                )
            }

        state.currentPlayer += state.direction
        state.currentPlayer %= state.players.size
        if (state.currentPlayer < 0) state.currentPlayer = state.players.size - abs(state.currentPlayer)
        return
    }

    // Play a card
    execBotTurn(state, player)
    logSystem(turn, "Bot ${player.bot.name} played ${state.pile.last().name()}.")
    turn.actions.addLast(Action(player.bot.botId, ActionType.PLAY_CARD, state.pile.last()))
    state.players
        .filter { p -> p != player }
        .forEach { p ->
            execBotEvent(
                state,
                p,
                "card-played",
                listOf(encodeCard(state.pile.last()), encodePlayer(player)),
            )
        }

    // Determine the next player
    if (state.pile.last().type == CardType.SWITCH) {
        // If only two players play its basically a skip card
        state.direction *= -1
        if (state.players.size != 2) {
            state.currentPlayer += state.direction
        }
    } else if (state.pile.last().type == CardType.SKIP) {
        state.currentPlayer += (state.direction * 2)
    } else {
        state.currentPlayer += state.direction
    }
    state.currentPlayer %= state.players.size
    if (state.currentPlayer < 0) state.currentPlayer = state.players.size - abs(state.currentPlayer)

    // Next player picks cards if necessary
    val nextPlayer = state.players[state.currentPlayer]
    if (state.pile.last().type == CardType.DRAW_TWO) {
        logSystem(turn, "Bot ${nextPlayer.bot.name} draws 2 cards.")
        pickupCards(state, nextPlayer, 2)
    } else if (state.pile.last().type == CardType.CHOOSE_DRAW) {
        logSystem(turn, "Bot ${nextPlayer.bot.name} draws 4 cards.")
        pickupCards(state, nextPlayer, 4)
    }
}

fun execBotTurn(
    state: GameState,
    player: Player,
) {
    val topCard = state.pile.last()

    // Call the bot
    val result: SchemeValue?
    try {
        val func = player.interpreter.env.get("turn")!!
        if (func !is CallableValue) {
            throw DisqualificationError(
                player.bot.botId,
                "`turn` isn't a function but a ${func.typeName()}",
                null,
            )
        }

        val players = state.players.map { p -> encodePlayer(p) }
        result =
            player.interpreter.run(
                func,
                listOf(
                    encodeCard(topCard),
                    ListValue(player.hand.map { c -> encodeCard(c) }, null),
                    ListValue(players, null),
                ),
            )
    } catch (e: SchemeError) {
        throw DisqualificationError(player.bot.botId, "The bot crashed while taking a turn.", e)
    }

    val logOutput = player.output.toString()
    player.output.clear()
    if (logOutput.isNotEmpty()) {
        logBot(state.turns.last(), player.bot, logOutput)
    }

    // Verify the played card and remove it from the hand
    val playedCard = decodeCard(result!!)

    if (playedCard.type == CardType.CHOOSE_DRAW || playedCard.type == CardType.CHOOSE) {
        if (!player.hand.remove(Card(playedCard.type, Color.ANY, null))) {
            throw DisqualificationError(
                player.bot.botId,
                "The bot tried to play a card it doesn't hold: $playedCard",
                null,
            )
        }
    } else {
        if (!player.hand.remove(playedCard)) {
            throw DisqualificationError(
                player.bot.botId,
                "The bot tried to play a card it doesn't hold: $playedCard",
                null,
            )
        }
    }

    if (!topCard.match(playedCard)) {
        throw DisqualificationError(player.bot.botId, "The bot tried to play an invalid card: $playedCard", null)
    }

    state.pile.addLast(playedCard)
}

fun execBotEvent(
    state: GameState,
    player: Player,
    funcName: String,
    arguments: List<SchemeValue>,
) {
    // Event handlers are optional
    if (!player.interpreter.env.has(funcName)) {
        return
    }

    val func = player.interpreter.env.get(funcName)!!
    if (func !is FuncValue) {
        throw DisqualificationError(
            player.bot.botId,
            "The event handler `$funcName` isn't a function but a ${func.typeName()}",
            null,
        )
    }

    val arity = arguments.size
    if (func.arity.min != arity || func.arity.max != arity) {
        throw DisqualificationError(
            player.bot.botId,
            "The event handler `$funcName` must accept exactly $arity arguments.",
            null,
        )
    }

    try {
        player.interpreter.run(
            func,
            arguments,
        )
    } catch (e: SchemeError) {
        throw DisqualificationError(player.bot.botId, "The bot crashed while handling the `$funcName` event.", e)
    }

    val logOutput = player.output.toString()
    player.output.clear()
    if (logOutput.isNotEmpty()) {
        logBot(state.turns.last(), player.bot, logOutput)
    }
}

fun turnSnapShot(state: GameState): Turn {
    return Turn(
        state.turns.size.toLong(),
        state.players[state.currentPlayer].bot.botId,
        state.pile.last(),
        state.drawPile.take(5),
        state.players.map { p -> Hand(p.bot.botId, p.hand.toList()) },
        mutableListOf(),
        mutableListOf(),
    )
}

fun pickupCards(
    state: GameState,
    player: Player,
    number: Int,
): List<Card> {
    // Reshuffle the pile if necessary
    if (number > state.drawPile.size) {
        logSystem(state.turns.last(), "Reshuffle pile into the draw pile.")
        var playedCards = state.pile.drop(1)

        // If played choose cards have a color, reset it
        playedCards =
            playedCards.map { card ->
                when (card.type) {
                    CardType.CHOOSE -> Card(CardType.CHOOSE, Color.ANY, null)
                    CardType.CHOOSE_DRAW -> Card(CardType.CHOOSE_DRAW, Color.ANY, null)
                    else -> card
                }
            }
        state.pile = state.pile.take(1).toMutableList()
        state.drawPile.addAll(playedCards.shuffled())

        state.players.forEach { p ->
            execBotEvent(state, p, "pile-reshuffled", listOf())
        }
    }

    // Pick up the cards
    var cards = state.drawPile.removeFirstN(number)
    for (card in cards) {
        state.turns.last().actions.addLast(Action(player.bot.botId, ActionType.DRAW_CARD, card))
    }
    player.hand.addAll(cards)
    return cards
}

fun initialGameState(
    deck: MutableList<Card>,
    request: SimulationRequest,
): GameState {
    val players =
        request.participatingBots.map { bot ->
            val buffer = StringBuilder()
            val interpreter = SchemeInterpreter(buffer)
            injectSimulationBuiltin(interpreter.env)
            Player(bot, deck.removeFirstN(7).toMutableList(), interpreter, buffer)
        }
    return GameState(mutableListOf(deck.removeFirst()), deck, players)
}

private fun initializeBots(players: List<Player>) {
    for (player in players) {
        try {
            player.interpreter.run(player.bot.code)
        } catch (e: SchemeError) {
            throw DisqualificationError(player.bot.botId, "The bot crashed during the initialization.", e)
        }

        if (!player.interpreter.env.has("turn")) {
            throw DisqualificationError(
                player.bot.botId,
                "The bot doesn't implement the required turn function.",
                null,
            )
        }

        val turnFunc = player.interpreter.env.get("turn")!!
        if (turnFunc !is FuncValue) {
            throw DisqualificationError(
                player.bot.botId,
                "`turn` is not a function but a ${turnFunc.typeName()}",
                null,
            )
        }

        if (turnFunc.arity.min != 3 || turnFunc.arity.max != 3) {
            throw DisqualificationError(
                player.bot.botId,
                "The `turn` function must accept exactly three arguments.",
                null,
            )
        }
    }
}

private fun logSystem(
    turn: Turn,
    message: String,
) {
    val turnText = "%02d".format(turn.turnId + 1)
    val fullMessage = "[$turnText] " + message
    logger.debug(fullMessage)
    turn.logMessages.addLast(SystemLogMessage(fullMessage))
}

private fun logBot(
    turn: Turn,
    bot: Bot,
    message: String,
) {
    logger.debug("Bot ${bot.name} prints: $message")
    turn.logMessages.addLast(DebugLogMessage(bot.botId, message))
}
