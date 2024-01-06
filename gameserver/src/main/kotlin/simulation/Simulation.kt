package simulation

import cardscheme.*
import simulation.models.*
import java.time.Instant
import kotlin.math.abs

class Simulation(val gameId: Long, val bots: List<Bot>) {
    private var pile: MutableList<Card> = mutableListOf()
    private var drawPile: MutableList<Card> = mutableListOf()
    private var players: List<Player> = mutableListOf()
    private var currentPlayer = 0
    private var direction = 1
    private var turns = mutableListOf<Turn>()

    fun run(): SimulationResult {
        val startTime = Instant.now()
        var winningBotId: Long? = null
        var disqualifiedBotId: Long? = null

        // FIXME: Like really, badly, that's is a crime to all programming gods.
        try {
            initializeGame()

            var isFirst = true
            turns.addLast(
                Turn(
                    turnId = turns.size.toLong(),
                    topCard = pile.last(),
                    drawPile = drawPile.take(10),
                    hands = players.map { p -> Hand(p.bot.botId, p.hand) },
                    actions = mutableListOf<Action>(),
                    logMessages = mutableListOf<LogMessage>(),
                ),
            )

            initializeBots()

            while (true) {
                if (!isFirst) {
                    turns.addLast(
                        Turn(
                            turnId = turns.size.toLong(),
                            topCard = pile.last(),
                            drawPile = drawPile.take(10),
                            hands = players.map { p -> Hand(p.bot.botId, p.hand) },
                            actions = mutableListOf<Action>(),
                            logMessages = mutableListOf<LogMessage>(),
                        ),
                    )
                }
                isFirst = false

                val player = players[currentPlayer]

                // Pick up cards if necessary
                if (pile.last().type == CardType.DRAW_TWO) {
                    pickup(player, 2)
                } else if (pile.last().type == CardType.CHOOSE_DRAW) {
                    pickup(player, 4)
                }

                // Play a card
                takeTurn(player)

                logSystem("Player ${player.bot.botId} played ${pile.last()}")
                if (player.hand.isEmpty()) {
                    winningBotId = player.bot.botId
                    logSystem("Player ${player.bot.botId} won!!!")
                    break
                }

                // Determine the next player
                if (pile.last().type == CardType.SWITCH) {
                    // If only two players play its basically a skip card
                    // TODO: Is this how this should work?
                    if (players.size != 2) {
                        direction *= -1
                        currentPlayer += direction
                    }
                } else if (pile.last().type == CardType.SKIP) {
                    currentPlayer += (direction * 2)
                } else {
                    currentPlayer += direction
                }
                currentPlayer %= players.size
                if (currentPlayer < 0) currentPlayer = players.size - abs(currentPlayer)
            }
        } catch (e: DisqualificationError) {
            val bot = bots.filter { b -> b.botId == e.botId }.first()
            logSystem("Player $bot.botId} disqualified.")
            logBot(bot, e.reason)
            if (e.schemeError != null) {
                // FIXME: Disable colors in the error
                logBot(bot, e.schemeError.format(bot.code))
            }
            disqualifiedBotId = e.botId
        }

        return SimulationResult(
            gameId = gameId,
            startTime = startTime,
            endTime = Instant.now(),
            winningBotId = winningBotId,
            disqualifiedBotId = disqualifiedBotId,
            turns = turns,
            participatingBotIds = bots.map { b -> b.botId },
        )
    }

    private fun initializeGame() {
        drawPile = generateDeck().shuffled().toMutableList()
        for (bot in bots) {
            val player = Player(bot, takeCards(7).toMutableList(), SchemeInterpreter())
            injectSimulationBuiltin(player.interpreter.env)
            players.addLast(player)
        }
        pile = takeCards(1).toMutableList()
    }

    private fun initializeBots() {
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

    private fun takeTurn(player: Player) {
        val topCard = pile.last()
        // Verify that the player has at least a matching card
        // FIXME: That's not how it works right?
        while (player.hand.none { c -> topCard.match(c) }) {
            logSystem("Player ${player.bot.botId} doesn't have any matching cards.")
            pickup(player, 1)
        }

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
                this.players.map { p ->
                    VectorValue(
                        mutableListOf(
                            StringValue(p.bot.botId.toString(), null),
                            IntegerValue(p.hand.size, null),
                        ),
                        null,
                    )
                }

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

        if (!player.hand.remove(playedCard)) {
            throw DisqualificationError(
                player.bot.botId,
                "The bot tried to play a card it doesn't hold: $playedCard",
                null,
            )
        }

        if (!topCard.match(playedCard)) {
            throw DisqualificationError(player.bot.botId, "The tried to play an invalid card: $playedCard", null)
        }

        pile.addLast(playedCard)
    }

    private fun pickup(
        player: Player,
        number: Int,
    ) {
        val cards = takeCards(number)
        player.hand.addAll(cards)
        for (card in cards) {
            logSystem("Player ${player.bot.botId} picked up $card.")
            currentTurn().actions.addLast(Action(player.bot.botId, ActionType.DRAW_CARD, card))
        }
    }

    private fun takeCards(number: Int): List<Card> {
        // Reshuffle the pile if necessary
        if (number > drawPile.size) {
            println("Reshuffle pile into the draw pile")
            val playedCards = pile.drop(1)
            pile = pile.take(1).toMutableList()
            drawPile.addAll(playedCards.shuffled())
        }

        val cards = drawPile.take(number)
        drawPile = drawPile.drop(number).toMutableList()
        return cards
    }

    private fun logSystem(message: String) {
        println(message)
        currentTurn().logMessages.addLast(SystemLogMessage(message))
    }

    private fun logBot(
        bot: Bot,
        message: String,
    ) {
        println("${bot.botId} prints: $message")
        currentTurn().logMessages.addLast(DebugLogMessage(bot.botId, message))
    }

    private fun currentTurn(): Turn {
        return turns.last()
    }
}
