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
        val buffer = StringBuilder()
        val interpreter = SchemeInterpreter(buffer)
        injectSimulationBuiltin(interpreter.env)
        interpreter.run(code)
        return Player(Bot(id, id, "Bot$id", code), hand, interpreter, buffer)
    }

    @Test
    fun runTurnBotPlaysOnlyCorrectCard() {
        // The bot has only one card and plays it.
        val player1 = createTestPlayer(0, randomBotCode, mutableListOf(Card(CardType.NUMBER_CARD, Color.CYAN, 1)))
        val player2 = createTestPlayer(1, randomBotCode, mutableListOf(Card(CardType.NUMBER_CARD, Color.GREEN, 1)))
        val state =
            GameState(
                mutableListOf(Card(CardType.NUMBER_CARD, Color.CYAN, 5)),
                mutableListOf(),
                listOf(player1, player2),
            )

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
            GameState(
                mutableListOf(Card(CardType.NUMBER_CARD, Color.CYAN, 5)),
                mutableListOf(),
                listOf(player1, player2),
            )

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
            GameState(
                mutableListOf(Card(CardType.NUMBER_CARD, Color.CYAN, 5)),
                mutableListOf(Card(CardType.SKIP, Color.PURPLE, null)),
                listOf(player1, player2),
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
            GameState(
                mutableListOf(Card(CardType.NUMBER_CARD, Color.CYAN, 5)),
                mutableListOf(Card(CardType.SKIP, Color.PURPLE, null)),
                listOf(player1, player2),
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
            GameState(
                mutableListOf(Card(CardType.NUMBER_CARD, Color.CYAN, 5)),
                mutableListOf(Card(CardType.SKIP, Color.PURPLE, null)),
                listOf(player1, player2),
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
            GameState(
                mutableListOf(Card(CardType.NUMBER_CARD, Color.CYAN, 5)),
                mutableListOf(Card(CardType.SKIP, Color.PURPLE, null)),
                listOf(player1, player2),
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
            GameState(
                mutableListOf(Card(CardType.NUMBER_CARD, Color.CYAN, 5)),
                mutableListOf(Card(CardType.SKIP, Color.PURPLE, null)),
                listOf(player1, player2),
            )

        runTurn(state)
        Assert.assertEquals(state.currentPlayer, 0)
        val expectedCard = Card(CardType.SKIP, Color.CYAN, null)
        Assert.assertEquals(state.turns.last().actions.first { a -> a.type == ActionType.PLAY_CARD }.card, expectedCard)
        Assert.assertEquals(state.pile.last(), expectedCard)
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
            GameState(
                mutableListOf(Card(CardType.NUMBER_CARD, Color.CYAN, 5)),
                mutableListOf(Card(CardType.SKIP, Color.PURPLE, null)),
                listOf(player1, player2),
            )

        runTurn(state)
        Assert.assertEquals(state.direction, -1)
        val expectedCard = Card(CardType.SWITCH, Color.CYAN, null)
        Assert.assertEquals(state.turns.last().actions.first { a -> a.type == ActionType.PLAY_CARD }.card, expectedCard)
        Assert.assertEquals(state.pile.last(), expectedCard)
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
            GameState(
                mutableListOf(Card(CardType.NUMBER_CARD, Color.CYAN, 5)),
                mutableListOf(Card(CardType.SKIP, Color.PURPLE, null), Card(CardType.CHOOSE, Color.ANY, null)),
                listOf(player1, player2),
            )

        runTurn(state)
        Assert.assertEquals(player1.hand.size, 0)
        Assert.assertEquals(player2.hand.size, 3)
        val expectedCard = Card(CardType.DRAW_TWO, Color.CYAN, null)
        Assert.assertEquals(state.turns.last().actions.first { a -> a.type == ActionType.PLAY_CARD }.card, expectedCard)
        Assert.assertEquals(state.pile.last(), expectedCard)
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
            GameState(
                mutableListOf(Card(CardType.NUMBER_CARD, Color.CYAN, 5)),
                mutableListOf(Card(CardType.SKIP, Color.PURPLE, null), Card(CardType.CHOOSE, Color.ANY, null)),
                listOf(player1, player2),
            )

        runTurn(state)
        assert(player1.hand.isEmpty())
        val expectedCard = Card(CardType.CHOOSE, Color.ANY, null)
        Assert.assertEquals(state.turns.last().actions.first { a -> a.type == ActionType.PLAY_CARD }.card, expectedCard)
        Assert.assertEquals(state.pile.last(), expectedCard)
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
            GameState(
                mutableListOf(Card(CardType.NUMBER_CARD, Color.CYAN, 5)),
                mutableListOf(
                    Card(CardType.SKIP, Color.PURPLE, null),
                    Card(CardType.CHOOSE, Color.ANY, null),
                    Card(CardType.NUMBER_CARD, Color.PURPLE, 4),
                    Card(CardType.NUMBER_CARD, Color.ORANGE, 0),
                ),
                listOf(player1, player2),
            )

        runTurn(state)
        assert(player1.hand.isEmpty())
        Assert.assertEquals(player2.hand.size, 5)
        val expectedCard = Card(CardType.CHOOSE_DRAW, Color.ANY, null)
        Assert.assertEquals(state.turns.last().actions.first { a -> a.type == ActionType.PLAY_CARD }.card, expectedCard)
        Assert.assertEquals(state.pile.last(), expectedCard)
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
            GameState(
                mutableListOf(Card(CardType.NUMBER_CARD, Color.CYAN, 5)),
                mutableListOf(Card(CardType.SKIP, Color.PURPLE, null), Card(CardType.CHOOSE, Color.ANY, null)),
                listOf(player1, player2),
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
            GameState(
                mutableListOf(Card(CardType.NUMBER_CARD, Color.CYAN, 5)),
                mutableListOf(
                    Card(CardType.SKIP, Color.PURPLE, null),
                    Card(CardType.CHOOSE, Color.ANY, null),
                    Card(CardType.NUMBER_CARD, Color.PURPLE, 4),
                    Card(CardType.NUMBER_CARD, Color.ORANGE, 0),
                ),
                listOf(player1, player2),
            )

        runTurn(state)
        assert(player1.hand.isEmpty())
        Assert.assertEquals(player2.hand.size, 5)
        val expectedCard = Card(CardType.CHOOSE_DRAW, Color.GREEN, null)
        Assert.assertEquals(state.pile.last(), expectedCard)
        Assert.assertEquals(state.turns.last().actions.first { a -> a.type == ActionType.PLAY_CARD }.card, expectedCard)
    }

    @Test
    fun runTurnBotLogsOutput() {
        val player1 =
            createTestPlayer(
                0,
                """
                (define (turn topCard hand players)
                        (display "I was here")
                        (random-choice
                            (matching-cards topCard hand)))
                """.trimIndent(),
                mutableListOf(
                    Card(CardType.SWITCH, Color.CYAN, null),
                ),
            )
        val player2 = createTestPlayer(1, randomBotCode, mutableListOf(Card(CardType.NUMBER_CARD, Color.GREEN, 1)))
        val state =
            GameState(
                mutableListOf(Card(CardType.NUMBER_CARD, Color.CYAN, 5)),
                mutableListOf(Card(CardType.SKIP, Color.PURPLE, null)),
                listOf(player1, player2),
            )

        runTurn(state)
        val logMessage = state.turns.last().logMessages.first { m -> m is DebugLogMessage } as DebugLogMessage
        Assert.assertEquals("I was here", logMessage.message)
        Assert.assertEquals(0, logMessage.botId)
    }

    @Test
    fun runTurnBotDoesnotLog() {
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
            GameState(
                mutableListOf(Card(CardType.NUMBER_CARD, Color.CYAN, 5)),
                mutableListOf(Card(CardType.SKIP, Color.PURPLE, null)),
                listOf(player1, player2),
            )

        runTurn(state)
        Assert.assertEquals(true, state.turns.last().logMessages.none { m -> m is DebugLogMessage })
    }

    @Test
    fun runTurnOtherBotListensToPlayed() {
        val code =
            """
            (define (turn topCard hand players)
                    (random-choice
                        (matching-cards topCard hand)))
                        
            (define (card-played card player)
                    (display "A new card was dropped")
            )
            """.trimIndent()
        val player1 = createTestPlayer(0, code, mutableListOf(Card(CardType.SWITCH, Color.CYAN, null)))
        val player2 = createTestPlayer(1, code, mutableListOf(Card(CardType.NUMBER_CARD, Color.GREEN, 1)))
        val state =
            GameState(
                mutableListOf(Card(CardType.NUMBER_CARD, Color.CYAN, 5)),
                mutableListOf(Card(CardType.SKIP, Color.PURPLE, null)),
                listOf(player1, player2),
            )

        runTurn(state)
        Assert.assertEquals(1, state.turns.last().logMessages.filter { m -> m is DebugLogMessage }.size)
        val logMessage = state.turns.last().logMessages.first { m -> m is DebugLogMessage } as DebugLogMessage
        Assert.assertEquals("A new card was dropped", logMessage.message)
        Assert.assertEquals(1, logMessage.botId)
    }

    @Test
    fun runBotsListenToReshuffleEvent() {
        val code =
            """
            (define (turn topCard hand players)
                    (random-choice
                        (matching-cards topCard hand)))
                        
            (define (pile-reshuffled)
                    (display "Shake it")) 
            """.trimIndent()

        val player1 = createTestPlayer(0, code, mutableListOf(Card(CardType.DRAW_TWO, Color.CYAN, null)))
        val player2 = createTestPlayer(1, code, mutableListOf(Card(CardType.NUMBER_CARD, Color.GREEN, 1)))
        val state =
            GameState(
                mutableListOf(
                    Card(CardType.NUMBER_CARD, Color.CYAN, 5),
                    Card(CardType.NUMBER_CARD, Color.PURPLE, 0),
                    Card(CardType.NUMBER_CARD, Color.ORANGE, 8),
                    Card(CardType.NUMBER_CARD, Color.GREEN, 3),
                    Card(CardType.NUMBER_CARD, Color.PURPLE, 2),
                ),
                mutableListOf(),
                listOf(player1, player2),
            )

        runTurn(state)
        Assert.assertEquals(2, state.turns.last().logMessages.filter { m -> m is DebugLogMessage }.size)
    }

    @Test
    fun runTurnOtherBotListensToPicked() {
        val code =
            """
            (define (turn topCard hand players)
                    (random-choice
                        (matching-cards topCard hand)))
                        
            (define (card-picked top-card player)
                    (display "Get those cards!")
            )
            """.trimIndent()
        val player1 = createTestPlayer(0, code, mutableListOf(Card(CardType.SWITCH, Color.ORANGE, null)))
        val player2 =
            createTestPlayer(
                1,
                code,
                mutableListOf(Card(CardType.NUMBER_CARD, Color.GREEN, 1)),
            )
        val state =
            GameState(
                mutableListOf(Card(CardType.NUMBER_CARD, Color.CYAN, 5)),
                mutableListOf(Card(CardType.SKIP, Color.PURPLE, null)),
                listOf(player1, player2),
            )

        runTurn(state)
        Assert.assertEquals(1, state.turns.last().logMessages.filter { m -> m is DebugLogMessage }.size)
        val logMessage = state.turns.last().logMessages.first { m -> m is DebugLogMessage } as DebugLogMessage
        Assert.assertEquals("Get those cards!", logMessage.message)
        Assert.assertEquals(1, logMessage.botId)
    }
}
