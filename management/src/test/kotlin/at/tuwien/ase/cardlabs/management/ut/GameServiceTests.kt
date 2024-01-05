package at.tuwien.ase.cardlabs.management.ut

import at.tuwien.ase.cardlabs.management.ApplicationTest
import at.tuwien.ase.cardlabs.management.TestHelper
import at.tuwien.ase.cardlabs.management.database.model.game.GameDAO
import at.tuwien.ase.cardlabs.management.database.model.game.GameState
import at.tuwien.ase.cardlabs.management.database.model.game.action.Action
import at.tuwien.ase.cardlabs.management.database.model.game.action.ActionType
import at.tuwien.ase.cardlabs.management.database.model.game.card.Card
import at.tuwien.ase.cardlabs.management.database.model.game.card.CardType
import at.tuwien.ase.cardlabs.management.database.model.game.card.Color
import at.tuwien.ase.cardlabs.management.database.model.game.hand.Hand
import at.tuwien.ase.cardlabs.management.database.model.game.log.DebugLogMessage
import at.tuwien.ase.cardlabs.management.database.model.game.log.SystemLogMessage
import at.tuwien.ase.cardlabs.management.database.model.game.turn.Turn
import at.tuwien.ase.cardlabs.management.database.repository.GameRepository
import at.tuwien.ase.cardlabs.management.error.game.GameDoesNotExistException
import at.tuwien.ase.cardlabs.management.service.AccountService
import at.tuwien.ase.cardlabs.management.service.game.GameService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import java.time.LocalDateTime
import java.time.ZoneOffset

@ApplicationTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GameServiceTests {

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var gameRepository: GameRepository

    @Autowired
    private lateinit var gameService: GameService

    @Test
    fun whenGameFetchAllById_widthNonExistingGame_expectGameDoesNotExistError() {
        val account = TestHelper.createAccount(accountService)
        val user = TestHelper.createUserDetails(account)
        val gameId = 0L

        val exception = assertThrows<GameDoesNotExistException> {
            gameService.fetchById(user, gameId)
        }
        assertEquals("A game with the id $gameId doesn't exist", exception.message)
    }

    @Test
    fun whenGameFetchAllById_withExistingGame_expectSuccess() {
        val account = TestHelper.createAccount(accountService)
        val user = TestHelper.createUserDetails(account)
        val botId = 0L

        val gameDAO = GameDAO()
        gameDAO.startTime = LocalDateTime.of(2023, 12, 11, 15, 0).toInstant(ZoneOffset.UTC)
        gameDAO.endTime = LocalDateTime.of(2023, 12, 11, 16, 0).toInstant(ZoneOffset.UTC)
        gameDAO.winningBotId = botId
        val topCard = Card(CardType.DRAW_TWO, Color.CYAN, null)
        val drawPile = listOf(
            Card(CardType.DRAW_TWO, Color.CYAN, null),
            Card(CardType.NUMBER_CARD, Color.ORANGE, 5),
        )
        val hand = listOf(
            Hand(botId, emptyList())
        )
        val actions = listOf(
            Action(
                botId,
                ActionType.PLAY_CARD,
                Card(CardType.DRAW_TWO, Color.ORANGE, null),
            )
        )
        val logMessages = listOf(
            SystemLogMessage("hello"),
            DebugLogMessage("world", botId),
        )
        gameDAO.turns = listOf(
            Turn(0, topCard, drawPile, hand, actions, logMessages),
        )
        gameDAO.gameState = GameState.CREATED
        gameDAO.participatingBotsId = listOf(botId)
        val gameId = gameRepository.save(gameDAO).id!!
        val result = gameService.fetchById(user, gameId)

        assertEquals(gameDAO.startTime, result.startTime)
        assertEquals(gameDAO.endTime, result.endTime)
        assertEquals(gameDAO.winningBotId, result.winningBotId)
        assertEquals(gameDAO.turns.size, result.turns.size)
        assertEquals(gameDAO.turns[0].turnId, result.turns[0].turnId)
        assertEquals(gameDAO.turns[0].topCard, result.turns[0].topCard)
        assertEquals(gameDAO.turns[0].drawPile.size, result.turns[0].drawPile.size)
        assertEquals(gameDAO.turns[0].drawPile[0], result.turns[0].drawPile[0])
        assertEquals(gameDAO.turns[0].hands.size, result.turns[0].hands.size)
        assertEquals(gameDAO.turns[0].hands[0], result.turns[0].hands[0])
        assertEquals(gameDAO.turns[0].actions.size, result.turns[0].actions.size)
        assertEquals(gameDAO.turns[0].actions[0], result.turns[0].actions[0])
        assertEquals(gameDAO.turns[0].logMessages.size, result.turns[0].logMessages.size)
        assertEquals(gameDAO.turns[0].logMessages[0], result.turns[0].logMessages[0])
        assertEquals(gameDAO.turns[0].logMessages[1], result.turns[0].logMessages[1])
        assertEquals(gameDAO.gameState, result.gameState)
    }

    @Test
    fun whenGameFetchLogById_widthNonExistingGame_expectGameDoesNotExistError() {
        val account = TestHelper.createAccount(accountService)
        val user = TestHelper.createUserDetails(account)
        val gameId = 0L

        val exception = assertThrows<GameDoesNotExistException> {
            gameService.fetchLogById(user, gameId)
        }
        assertEquals("A game with the id $gameId doesn't exist", exception.message)
    }

    @Test
    fun whenGameFetchLogById_withExistingGame_expectSuccess() {
        val account = TestHelper.createAccount(accountService)
        val user = TestHelper.createUserDetails(account)
        val botId = 0L

        val gameDAO = GameDAO()
        gameDAO.startTime = LocalDateTime.of(2023, 12, 11, 15, 0).toInstant(ZoneOffset.UTC)
        gameDAO.endTime = LocalDateTime.of(2023, 12, 11, 16, 0).toInstant(ZoneOffset.UTC)
        gameDAO.winningBotId = botId
        val topCard = Card(CardType.DRAW_TWO, Color.CYAN, null)
        val logMessages = listOf(
            SystemLogMessage("hello"),
            DebugLogMessage("world", botId),
        )
        gameDAO.turns = listOf(
            Turn(0, topCard, emptyList(), emptyList(), emptyList(), logMessages),
        )
        gameDAO.gameState = GameState.CREATED
        gameDAO.participatingBotsId = listOf(botId)
        val gameId = gameRepository.save(gameDAO).id!!
        val result = gameService.fetchLogById(user, gameId)

        assertEquals(gameDAO.turns[0].logMessages.size, result.size)
        assertEquals(gameDAO.turns[0].logMessages[0], result[0])
        assertEquals(gameDAO.turns[0].logMessages[1], result[1])
    }
}
