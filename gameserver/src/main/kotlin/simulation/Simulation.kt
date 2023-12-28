package simulation

import cardscheme.*
import kotlin.math.abs

class Simulation {
    private var pile: MutableList<Card> = mutableListOf()
    private var drawPile: MutableList<Card> = mutableListOf()
    private var players: List<Player> = mutableListOf()
    private var currentPlayer = 0
    private var direction = 1

    fun run(bots: List<Bot>) {
        drawPile = generateDeck().shuffled().toMutableList()
        for (bot in bots) {
            val player = Player(bot, pickup(7).toMutableList(), SchemeInterpreter())
            injectSimulationBuiltin(player.interpreter.env)
            players.addLast(player)
        }
        pile = pickup(1).toMutableList()
        println("Initial card is ${pile.last()}")

        // Verify all players
        for (player in players) {
            try {
                player.interpreter.run(player.bot.code)
            } catch (e: SchemeError) {
                throw DisqualificationError(player.bot.id, "The bot crashed during the initialization.", e)
            }

            if (!player.interpreter.env.has("turn")) {
                throw DisqualificationError(
                    player.bot.id,
                    "The bot doesn't implement the required turn function.",
                    null,
                )
            }

            // FIXME: Verify arity of the function
        }

        while (true) {
            val player = players[currentPlayer]

            // Pick up cards if necessary
            if (pile.last() is DrawCard) {
                println("Player ${player.bot.id} draws a card")
                println("Player ${player.bot.id} draws a card")
                player.hand.addAll(pickup(2))
            } else if (pile.last() is ChooseDrawCard) {
                println("Player ${player.bot.id} draws a card")
                println("Player ${player.bot.id} draws a card")
                println("Player ${player.bot.id} draws a card")
                println("Player ${player.bot.id} draws a card")
                player.hand.addAll(pickup(4))
            }

            // Play a card
            takeTurn(player)
            println("Player ${player.bot.id} played ${pile.last()}")
            if (player.hand.isEmpty()) {
                println("Player ${player.bot.id} won!!!")
                break
            }

            // Determine the next player
            if (pile.last() is SwitchCard) {
                // If only two players play its basically a skip card
                // TODO: Is this how this should work
                if (players.size != 2) {
                    direction *= -1
                    currentPlayer += direction
                }
            } else if (pile.last() is SkipCard) {
                currentPlayer += (direction * 2)
            } else {
                currentPlayer += direction
            }
            currentPlayer %= players.size
            if (currentPlayer < 0) currentPlayer = players.size - abs(currentPlayer)
        }
    }

    private fun takeTurn(player: Player) {
        val topCard = pile.last()
        // Verify that the player has at least a matching card
        while (player.hand.none { c -> topCard.match(c) }) {
            println("Player ${player.bot.id} draws a card")
            player.hand.addLast(pickup(1).first())
        }

        // Call the bot
        val result: SchemeValue?
        try {
            val func = player.interpreter.env.get("turn")!!
            if (func !is CallableValue) {
                throw DisqualificationError(player.bot.id, "`turn` isn't a function but a `${func.typeName()}`", null)
            }

            val players =
                this.players.map { p -> VectorValue(mutableListOf(StringValue(p.bot.name, null), IntegerValue(p.hand.size, null)), null) }

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
            throw DisqualificationError(player.bot.id, "The bot crashed while taking a turn.", e)
        }

        // Verify the played card and remove it from the hand
        val playedCard = decodeCard(result!!)

        if (!player.hand.remove(playedCard)) {
            throw DisqualificationError(
                player.bot.id,
                "The bot tried to play a card it doesn't hold: $playedCard",
                null,
            )
        }

        if (!topCard.match(playedCard)) {
            throw DisqualificationError(player.bot.id, "The tried to play an invalid card: $playedCard", null)
        }

        pile.addLast(playedCard)
    }

    private fun pickup(number: Int): List<Card> {
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
}
