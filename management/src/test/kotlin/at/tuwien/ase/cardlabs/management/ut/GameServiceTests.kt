package at.tuwien.ase.cardlabs.management.ut

import at.tuwien.ase.cardlabs.management.TestHelper
import at.tuwien.ase.cardlabs.management.database.model.match.GameDAO
import at.tuwien.ase.cardlabs.management.database.model.match.action.Action
import at.tuwien.ase.cardlabs.management.database.model.match.action.ActionType
import at.tuwien.ase.cardlabs.management.database.model.match.card.Card
import at.tuwien.ase.cardlabs.management.database.model.match.card.CardType
import at.tuwien.ase.cardlabs.management.database.model.match.card.Color
import at.tuwien.ase.cardlabs.management.database.model.match.log.DebugLogMessage
import at.tuwien.ase.cardlabs.management.database.model.match.log.SystemLogMessage
import at.tuwien.ase.cardlabs.management.database.model.match.result.Result
import at.tuwien.ase.cardlabs.management.database.repository.GameRepository
import at.tuwien.ase.cardlabs.management.error.game.GameDoesNotExistException
import at.tuwien.ase.cardlabs.management.service.AccountService
import at.tuwien.ase.cardlabs.management.service.match.GameService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import java.time.LocalDateTime

@SpringBootTest
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
        val botCodeId = 0L

        val gameDAO = GameDAO()
        gameDAO.startTime = LocalDateTime.of(2023, 12, 11, 15, 0)
        gameDAO.endTime = LocalDateTime.of(2023, 12, 11, 16, 0)
        gameDAO.actions = listOf(
            Action(botId, ActionType.INITIAL_CARD_DRAW, Card(CardType.DRAW_TWO, Color.CYAN, null), null),
            Action(botId, ActionType.DRAW_CARD, Card(CardType.NUMBER_CARD, Color.ORANGE, 5), null),
        )
        gameDAO.results = listOf(
            Result(botId, botCodeId, 1000, 1015, 0),
        )
        gameDAO.logMessages = listOf(
            SystemLogMessage(0, "Test message for system"),
            DebugLogMessage(0, "Test message for debug", botId),
        )
        val gameId = gameRepository.save(gameDAO).id!!
        val result = gameService.fetchById(user, gameId)

        assertEquals(gameDAO.startTime, result.startTime)
        assertEquals(gameDAO.endTime, result.endTime)
        assertEquals(gameDAO.actions.size, result.actions.size)
        assertEquals(gameDAO.actions[0], result.actions[0])
        assertEquals(gameDAO.actions[1], result.actions[1])
        assertEquals(gameDAO.results.size, result.results.size)
        assertEquals(gameDAO.results[0], result.results[0])
        assertEquals(gameDAO.logMessages.size, result.logMessages.size)
        assertEquals(gameDAO.logMessages[0], result.logMessages[0])
        assertEquals(gameDAO.logMessages[1], result.logMessages[1])
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
        gameDAO.startTime = LocalDateTime.of(2023, 12, 11, 15, 0)
        gameDAO.endTime = LocalDateTime.of(2023, 12, 11, 16, 0)
        gameDAO.actions = emptyList()
        gameDAO.results = emptyList()
        gameDAO.logMessages = listOf(
            SystemLogMessage(0, "Test message for system"),
            DebugLogMessage(0, "Test message for debug", botId),
        )
        val gameId = gameRepository.save(gameDAO).id!!
        val result = gameService.fetchLogById(user, gameId)

        assertEquals(gameDAO.logMessages.size, result.size)
        assertEquals(gameDAO.logMessages[0], result[0])
        assertEquals(gameDAO.logMessages[1], result[1])
    }
}
