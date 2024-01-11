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
                logSystem(state.turns.last(), "Player ${player.bot.botId} won!")
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

        logSystem(turn, "Bot ${bot.botId} disqualified")
        logBot(turn, bot, e.reason)
        if (e.schemeError != null) {
            // FIXME: Disable colors in the error
            logBot(turn, bot, e.schemeError.format(bot.code))
        }
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
        logSystem(turn, "Player ${player.bot.botId} has no matching card and draws one")
        pickupCards(state, player, 1)

        state.currentPlayer += state.direction
        state.currentPlayer %= state.players.size
        if (state.currentPlayer < 0) state.currentPlayer = state.players.size - abs(state.currentPlayer)
        return
    }

    // Play a card
    execBotTurn(state, player)
    logSystem(turn, "Player ${player.bot.botId} played ${state.pile.last()}")
    turn.actions.addLast(Action(player.bot.botId, ActionType.PLAY_CARD, state.pile.last()))

    // Determine the next player
    if (state.pile.last().type == CardType.SWITCH) {
        // If only two players play its basically a skip card
        // TODO: Is this how this should work?
        if (state.players.size != 2) {
            state.direction *= -1
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
        logSystem(turn, "Player ${nextPlayer.bot.botId} draws 2 cards.")
        pickupCards(state, nextPlayer, 2)
    } else if (state.pile.last().type == CardType.CHOOSE_DRAW) {
        logSystem(turn, "Player ${nextPlayer.bot.botId} draws 4 cards.")
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
                "`turn` isn't a function but a `${func.typeName()}`",
                null,
            )
        }

        val players =
            state.players.map { p ->
                VectorValue(
                    mutableListOf(
                        StringValue(p.bot.botId.toString(), null),
                        IntegerValue(p.hand.size, null),
                    ),
                    null,
                )
            }

        // FIXME: Add the output to the turn
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

fun turnSnapShot(state: GameState): Turn {
    return Turn(
        state.turns.size.toLong(),
        state.pile.last(),
        state.drawPile.take(5),
        state.players.map { p -> Hand(p.bot.botId, p.hand) },
        mutableListOf(),
        mutableListOf(),
    )
}

fun pickupCards(state: GameState, player: Player, number: Int) {
    // Reshuffle the pile if necessary
    if (number > state.drawPile.size) {
        logSystem(state.turns.last(), "Reshuffle pile into the draw pile")
        var playedCards = state.pile.drop(1)

        // If played choose cards have a color, reset it
        playedCards = playedCards.map { card ->
            when (card.type) {
                CardType.CHOOSE -> Card(CardType.CHOOSE, Color.ANY, null)
                CardType.CHOOSE_DRAW -> Card(CardType.CHOOSE_DRAW, Color.ANY, null)
                else -> card
            }
        }
        state.pile = state.pile.take(1).toMutableList()
        state.drawPile.addAll(playedCards.shuffled())
    }

    // Pick up the cards
    var cards = state.drawPile.removeFirstN(number)
    for (card in cards) {
        state.turns.last().actions.addLast(Action(player.bot.botId, ActionType.DRAW_CARD, card))
    }
    player.hand.addAll(cards)
}

fun initialGameState(
    deck: MutableList<Card>,
    request: SimulationRequest,
): GameState {
    val players =
        request.participatingBots.map { bot ->
            val interpreter = SchemeInterpreter()
            injectSimulationBuiltin(interpreter.env)
            Player(bot, deck.removeFirstN(7).toMutableList(), interpreter)
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

        // FIXME: Verify arity of the function
    }
}

private fun logSystem(
    turn: Turn,
    message: String,
) {
    logger.debug(message)
    turn.logMessages.addLast(SystemLogMessage(message))
}

private fun logBot(
    turn: Turn,
    bot: Bot,
    message: String,
) {
    logger.debug("Bot ${bot.botId} prints: $message")
    turn.logMessages.addLast(DebugLogMessage(bot.botId, message))
}
