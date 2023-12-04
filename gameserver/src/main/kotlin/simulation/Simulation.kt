package simulation

import cardscheme.*

class Simulation {
    var pile: List<Card> = mutableListOf()
    var drawPile: List<Card> = mutableListOf()
    var players: List<Player> = listOf()
    var currentPlayer = 0
    var direction = 1

    fun run(bots: List<Bot>) {
        drawPile = generateDeck().shuffled()
        for (bot in bots) {
            players.addLast(Player(bot, pickup(7).toMutableList(), SchemeInterpreter()))
        }
        pile = pickup(1).toMutableList()

        // Verify all players
        for (player in players) {
            // FIXME: Inject custom methods here.

            try {
                player.interpreter.run(player.bot.code)
            } catch (e: SchemeError) {
                throw DisqualificationError(player.bot.id, "The bot crashed during the initialization.", e)
            }

            if (!player.interpreter.env.has("turn")) {
                throw DisqualificationError(player.bot.id, "The bot doesn't implement the required turn function.", null)
            }

            // FIXME: Verify arity of the function
        }

        while (true) {
            val player = players[currentPlayer]
            takeTurn(player)
            if (player.hand.isEmpty()) {
                println("Player ${player.bot.id} won!!!")
                break
            }
            println("Player ${player.bot.id} played ${pile.last()}")

            if (pile.last() is SwitchCard) {
                direction *= -1
                currentPlayer += direction
            } else if (pile.last() is SkipCard) {
                currentPlayer += direction * 2
            } else {
                currentPlayer += direction
            }
        }
    }

    fun takeTurn(player: Player) {
        val topCard = pile.last()
        // Verify that the player has at least a matching card
        while (player.hand.none { c -> topCard.match(c) }) {
            println("Bot ${player.bot.id} draws a card")
            player.hand.addLast(pickup(1).first())
        }

        // Call the bot
        val func = player.interpreter.env.get("turn")!!
        if (func !is CallableValue) {
            throw DisqualificationError(player.bot.id, "`turn` isn't a function but a `${func.typeName()}`", null)
        }

        val result: SchemeValue?
        try {
            result = player.interpreter.run(
                func,
                listOf<SchemeValue>(
                    encodeCard(topCard),
                    ListValue(SchemeList(player.hand.map { c -> encodeCard(c) })),
                    ListValue(SchemeList<SchemeValue>()),
                ),
            )
        } catch (e: SchemeError) {
            throw DisqualificationError(player.bot.id, "The bot crashed while taking a turn.", e)
        }

        // Verify the played card and remove it from the hand
        val playedCard = decodeCard(result!!)

        if (!player.hand.remove(playedCard)) {
            throw DisqualificationError(player.bot.id, "The bot tried to play a card it doesn't hold: $playedCard", null)
        }

        if (!topCard.match(playedCard)) {
            throw DisqualificationError(player.bot.id, "The tried to play an invalid card: $playedCard", null)
        }

        pile.addLast(playedCard)
    }

    fun pickup(number: Int): List<Card> {
        val cards = drawPile.take(number)
        drawPile = drawPile.drop(number)
        return cards
    }
}
