package simulation

import cardscheme.SchemeInterpreter
import org.junit.Assert
import org.junit.Test
import simulation.models.*

class SimulationTests {
    val randomBotCode =
        """
        (define (turn topCard hand players)
                (random-choice
                    (matching-cards topCard hand)))
        """.trimIndent()

    private fun createTestPlayer(
        id: Long,
        code: String,
        hand: MutableList<Card>,
    ): Player {
        val interpreter = SchemeInterpreter()
        injectSimulationBuiltin(interpreter.env)
        interpreter.run(code)
        return Player(Bot(id, id, code), hand, interpreter)
    }

    private fun createTestState(
        players: List<Player>,
        topCard: Card,
        drawPile: MutableList<Card>,
    ): GameState {
        return GameState(
            mutableListOf(topCard),
            drawPile,
            players,
            currentPlayer = 0,
            direction = 1,
            turns = mutableListOf(),
        )
    }

    @Test
    fun runTurnBotPlaysOnlyCorrectCard() {
        // The bot has only one card and plays it.
        val player1 = createTestPlayer(0, randomBotCode, mutableListOf(Card(CardType.NUMBER_CARD, Color.CYAN, 1)))
        val player2 = createTestPlayer(1, randomBotCode, mutableListOf(Card(CardType.NUMBER_CARD, Color.GREEN, 1)))
        val state =
            createTestState(listOf(player1, player2), Card(CardType.NUMBER_CARD, Color.CYAN, 5), mutableListOf())

        runTurn(state)
        assert(player1.hand.isEmpty())
        val expectedCard = (Card(CardType.NUMBER_CARD, Color.CYAN, 1))
        Assert.assertEquals(state.pile.last(), expectedCard)
        Assert.assertEquals(state.turns.last().actions.first { a -> a.type == ActionType.PLAY_CARD }.card, expectedCard)
        Assert.assertTrue(state.turns.last().actions.none { a -> a.type == ActionType.DRAW_CARD })
    }

    @Test
    fun runTurnBotPlaysCorrectCard() {
        // The bot has multiple cards, but only one matches
        val player1 =
            createTestPlayer(
                0,
                randomBotCode,
                mutableListOf(
                    Card(CardType.NUMBER_CARD, Color.CYAN, 1),
                    Card(CardType.NUMBER_CARD, Color.ORANGE, 4),
                    Card(CardType.NUMBER_CARD, Color.GREEN, 7),
                ),
            )
        val player2 = createTestPlayer(1, randomBotCode, mutableListOf(Card(CardType.NUMBER_CARD, Color.GREEN, 1)))
        val state =
            createTestState(listOf(player1, player2), Card(CardType.NUMBER_CARD, Color.CYAN, 5), mutableListOf())

        runTurn(state)

        Assert.assertEquals(player1.hand.size, 2)
        val expectedCard = (Card(CardType.NUMBER_CARD, Color.CYAN, 1))
        Assert.assertEquals(state.pile.last(), expectedCard)
        Assert.assertEquals(state.turns.last().actions.first { a -> a.type == ActionType.PLAY_CARD }.card, expectedCard)
        Assert.assertTrue(state.turns.last().actions.none { a -> a.type == ActionType.DRAW_CARD })
    }

    @Test
    fun runTurnBotDrawsCorrectCard() {
        // The bot doesn't have any matching cards, and needs to draw one
        val player1 =
            createTestPlayer(
                0,
                randomBotCode,
                mutableListOf(
                    Card(CardType.NUMBER_CARD, Color.ORANGE, 4),
                    Card(CardType.NUMBER_CARD, Color.GREEN, 7),
                ),
            )
        val player2 = createTestPlayer(1, randomBotCode, mutableListOf(Card(CardType.NUMBER_CARD, Color.GREEN, 1)))
        val state =
            createTestState(
                listOf(player1, player2),
                Card(CardType.NUMBER_CARD, Color.CYAN, 5),
                mutableListOf(Card(CardType.SKIP, Color.PURPLE, null)),
            )

        runTurn(state)

        Assert.assertEquals(player1.hand.size, 3)
        val expectedCard = Card(CardType.SKIP, Color.PURPLE, null)
        Assert.assertTrue(player1.hand.contains(expectedCard))
        Assert.assertEquals(state.turns.last().actions.first { a -> a.type == ActionType.DRAW_CARD }.card, expectedCard)
        Assert.assertTrue(state.turns.last().actions.none { a -> a.type == ActionType.PLAY_CARD })
    }

    @Test
    fun runTurnBotPlaysCardDoesnotMatch() {
        // The bot always plays the first card even if it's not correct
        val firstCardCode =
            """
            (define (turn topCard hand players)
             (car hand)) 
            """.trimIndent()

        val player1 =
            createTestPlayer(
                0,
                firstCardCode,
                mutableListOf(
                    Card(CardType.NUMBER_CARD, Color.GREEN, 7),
                    Card(CardType.NUMBER_CARD, Color.CYAN, 7),
                ),
            )
        val player2 = createTestPlayer(1, randomBotCode, mutableListOf(Card(CardType.NUMBER_CARD, Color.GREEN, 1)))
        val state =
            createTestState(
                listOf(player1, player2),
                Card(CardType.NUMBER_CARD, Color.CYAN, 5),
                mutableListOf(Card(CardType.SKIP, Color.PURPLE, null)),
            )

        Assert.assertThrows(DisqualificationError::class.java) { runTurn(state) }
    }

    @Test
    fun runTurnBotPlaysCardDoesnotHold() {
        // The bot always plays the first card even if it's not correct
        val firstCardCode =
            """
            (define (turn topCard hand players)
             (vector 'purple 'skip)) 
            """.trimIndent()

        val player1 =
            createTestPlayer(
                0,
                firstCardCode,
                mutableListOf(
                    Card(CardType.NUMBER_CARD, Color.GREEN, 7),
                    Card(CardType.NUMBER_CARD, Color.CYAN, 7),
                ),
            )
        val player2 = createTestPlayer(1, randomBotCode, mutableListOf(Card(CardType.NUMBER_CARD, Color.GREEN, 1)))
        val state =
            createTestState(
                listOf(player1, player2),
                Card(CardType.NUMBER_CARD, Color.CYAN, 5),
                mutableListOf(Card(CardType.SKIP, Color.PURPLE, null)),
            )

        Assert.assertThrows(DisqualificationError::class.java) { runTurn(state) }
    }

    @Test
    fun runTurnBotCrashes() {
        // The bot always crashes
        val crashCode =
            """
            (define (turn topCard hand players)
             (+ 1 "two")) 
            """.trimIndent()

        val player1 =
            createTestPlayer(
                0,
                crashCode,
                mutableListOf(
                    Card(CardType.NUMBER_CARD, Color.GREEN, 7),
                    Card(CardType.NUMBER_CARD, Color.CYAN, 7),
                ),
            )
        val player2 = createTestPlayer(1, randomBotCode, mutableListOf(Card(CardType.NUMBER_CARD, Color.GREEN, 1)))
        val state =
            createTestState(
                listOf(player1, player2),
                Card(CardType.NUMBER_CARD, Color.CYAN, 5),
                mutableListOf(Card(CardType.SKIP, Color.PURPLE, null)),
            )

        Assert.assertThrows(DisqualificationError::class.java) { runTurn(state) }
    }

    @Test
    fun runTurnBotPlaysSkipCard() {
        val player1 =
            createTestPlayer(
                0,
                randomBotCode,
                mutableListOf(
                    Card(CardType.SKIP, Color.CYAN, null),
                ),
            )
        val player2 = createTestPlayer(1, randomBotCode, mutableListOf(Card(CardType.NUMBER_CARD, Color.GREEN, 1)))
        val state =
            createTestState(
                listOf(player1, player2),
                Card(CardType.NUMBER_CARD, Color.CYAN, 5),
                mutableListOf(Card(CardType.SKIP, Color.PURPLE, null)),
            )

        runTurn(state)
        Assert.assertEquals(state.currentPlayer, 0)
    }

    @Test
    fun runTurnBotPlaysSwitchCard() {
        val player1 =
            createTestPlayer(
                0,
                randomBotCode,
                mutableListOf(
                    Card(CardType.SWITCH, Color.CYAN, null),
                ),
            )
        val player2 = createTestPlayer(1, randomBotCode, mutableListOf(Card(CardType.NUMBER_CARD, Color.GREEN, 1)))
        val state =
            createTestState(
                listOf(player1, player2),
                Card(CardType.NUMBER_CARD, Color.CYAN, 5),
                mutableListOf(Card(CardType.SKIP, Color.PURPLE, null)),
            )

        runTurn(state)
        Assert.assertEquals(state.direction, -1)
    }

    @Test
    fun runTurnBotPlaysDrawCard() {
        val player1 =
            createTestPlayer(
                0,
                randomBotCode,
                mutableListOf(
                    Card(CardType.DRAW_TWO, Color.CYAN, null),
                ),
            )
        val player2 = createTestPlayer(1, randomBotCode, mutableListOf(Card(CardType.NUMBER_CARD, Color.GREEN, 1)))
        val state =
            createTestState(
                listOf(player1, player2),
                Card(CardType.NUMBER_CARD, Color.CYAN, 5),
                mutableListOf(
                    Card(CardType.SKIP, Color.PURPLE, null),
                    Card(CardType.CHOOSE, Color.ANY, null),
                ),
            )

        runTurn(state)
        Assert.assertEquals(state.direction, -1)
        Assert.assertEquals(state.currentPlayer, 0)
    }

    @Test
    fun runTurnBotPlaysChooseCardWithoutWishing() {
        val player1 =
            createTestPlayer(
                0,
                randomBotCode,
                mutableListOf(
                    Card(CardType.CHOOSE, Color.ANY, null),
                ),
            )
        val player2 = createTestPlayer(1, randomBotCode, mutableListOf(Card(CardType.NUMBER_CARD, Color.GREEN, 1)))
        val state =
            createTestState(
                listOf(player1, player2),
                Card(CardType.NUMBER_CARD, Color.CYAN, 5),
                mutableListOf(
                    Card(CardType.SKIP, Color.PURPLE, null),
                    Card(CardType.CHOOSE, Color.ANY, null),
                ),
            )

        runTurn(state)
        assert(player1.hand.isEmpty())
    }

    @Test
    fun runTurnBotPlaysChooseDrawCardWithoutWishing() {
        val player1 =
            createTestPlayer(
                0,
                randomBotCode,
                mutableListOf(
                    Card(CardType.CHOOSE_DRAW, Color.ANY, null),
                ),
            )
        val player2 = createTestPlayer(1, randomBotCode, mutableListOf(Card(CardType.NUMBER_CARD, Color.GREEN, 1)))
        val state =
            createTestState(
                listOf(player1, player2),
                Card(CardType.NUMBER_CARD, Color.CYAN, 5),
                mutableListOf(
                    Card(CardType.SKIP, Color.PURPLE, null),
                    Card(CardType.CHOOSE, Color.ANY, null),
                    Card(CardType.NUMBER_CARD, Color.PURPLE, 4),
                    Card(CardType.NUMBER_CARD, Color.ORANGE, 0),
                ),
            )

        runTurn(state)
        assert(player1.hand.isEmpty())
        Assert.assertEquals(player2.hand.size, 5)
    }

    @Test
    fun runTurnBotPlaysChooseCardWithWishing() {
        val wishGreenCode =
            """
            (define (turn topCard hand players)
                    (vector 'green 'choose)) 
            """.trimIndent()
        val player1 =
            createTestPlayer(
                0,
                wishGreenCode,
                mutableListOf(
                    Card(CardType.CHOOSE, Color.ANY, null),
                ),
            )
        val player2 = createTestPlayer(1, randomBotCode, mutableListOf(Card(CardType.NUMBER_CARD, Color.GREEN, 1)))
        val state =
            createTestState(
                listOf(player1, player2),
                Card(CardType.NUMBER_CARD, Color.CYAN, 5),
                mutableListOf(
                    Card(CardType.SKIP, Color.PURPLE, null),
                    Card(CardType.CHOOSE, Color.ANY, null),
                ),
            )

        runTurn(state)
        assert(player1.hand.isEmpty())
        val expectedCard = Card(CardType.CHOOSE, Color.GREEN, null)
        Assert.assertEquals(state.pile.last(), expectedCard)
        Assert.assertEquals(state.turns.last().actions.first { a -> a.type == ActionType.PLAY_CARD }.card, expectedCard)
    }

    @Test
    fun runTurnBotPlaysChooseDrawCardWithWishing() {
        val wishGreenCode =
            """
            (define (turn topCard hand players)
                    (vector 'green 'choose-draw)) 
            """.trimIndent()
        val player1 =
            createTestPlayer(
                0,
                wishGreenCode,
                mutableListOf(
                    Card(CardType.CHOOSE_DRAW, Color.ANY, null),
                ),
            )
        val player2 = createTestPlayer(1, randomBotCode, mutableListOf(Card(CardType.NUMBER_CARD, Color.GREEN, 1)))
        val state =
            createTestState(
                listOf(player1, player2),
                Card(CardType.NUMBER_CARD, Color.CYAN, 5),
                mutableListOf(
                    Card(CardType.SKIP, Color.PURPLE, null),
                    Card(CardType.CHOOSE, Color.ANY, null),
                    Card(CardType.NUMBER_CARD, Color.PURPLE, 4),
                    Card(CardType.NUMBER_CARD, Color.ORANGE, 0),
                ),
            )

        runTurn(state)
        assert(player1.hand.isEmpty())
        Assert.assertEquals(player2.hand.size, 5)
        val expectedCard = Card(CardType.CHOOSE_DRAW, Color.GREEN, null)
        Assert.assertEquals(state.pile.last(), expectedCard)
        Assert.assertEquals(state.turns.last().actions.first { a -> a.type == ActionType.PLAY_CARD }.card, expectedCard)
    }
}
