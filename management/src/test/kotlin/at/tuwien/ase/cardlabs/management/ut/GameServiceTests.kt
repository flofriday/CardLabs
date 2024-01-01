package at.tuwien.ase.cardlabs.management.ut

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
import at.tuwien.ase.cardlabs.management.database.model.game.round.Round
import at.tuwien.ase.cardlabs.management.database.repository.GameRepository
import at.tuwien.ase.cardlabs.management.error.game.GameDoesNotExistException
import at.tuwien.ase.cardlabs.management.service.AccountService
import at.tuwien.ase.cardlabs.management.service.game.GameService
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

        val gameDAO = GameDAO()
        gameDAO.startTime = LocalDateTime.of(2023, 12, 11, 15, 0)
        gameDAO.endTime = LocalDateTime.of(2023, 12, 11, 16, 0)
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
        gameDAO.rounds = listOf(
            Round(0, topCard, drawPile, hand, actions, logMessages),
        )
        gameDAO.gameState = GameState.CREATED
        val gameId = gameRepository.save(gameDAO).id!!
        val result = gameService.fetchById(user, gameId)

        assertEquals(gameDAO.startTime, result.startTime)
        assertEquals(gameDAO.endTime, result.endTime)
        assertEquals(gameDAO.winningBotId, result.winningBotId)
        assertEquals(gameDAO.rounds.size, result.rounds.size)
        assertEquals(gameDAO.rounds[0].roundId, result.rounds[0].roundId)
        assertEquals(gameDAO.rounds[0].topCard, result.rounds[0].topCard)
        assertEquals(gameDAO.rounds[0].drawPile.size, result.rounds[0].drawPile.size)
        assertEquals(gameDAO.rounds[0].drawPile[0], result.rounds[0].drawPile[0])
        assertEquals(gameDAO.rounds[0].hand.size, result.rounds[0].hand.size)
        assertEquals(gameDAO.rounds[0].hand[0], result.rounds[0].hand[0])
        assertEquals(gameDAO.rounds[0].actions.size, result.rounds[0].actions.size)
        assertEquals(gameDAO.rounds[0].actions[0], result.rounds[0].actions[0])
        assertEquals(gameDAO.rounds[0].logMessages.size, result.rounds[0].logMessages.size)
        assertEquals(gameDAO.rounds[0].logMessages[0], result.rounds[0].logMessages[0])
        assertEquals(gameDAO.rounds[0].logMessages[1], result.rounds[0].logMessages[1])
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
        gameDAO.startTime = LocalDateTime.of(2023, 12, 11, 15, 0)
        gameDAO.endTime = LocalDateTime.of(2023, 12, 11, 16, 0)
        gameDAO.winningBotId = botId
        val topCard = Card(CardType.DRAW_TWO, Color.CYAN, null)
        val logMessages = listOf(
            SystemLogMessage("hello"),
            DebugLogMessage("world", botId),
        )
        gameDAO.rounds = listOf(
            Round(0, topCard, emptyList(), emptyList(), emptyList(), logMessages),
        )
        gameDAO.gameState = GameState.CREATED
        val gameId = gameRepository.save(gameDAO).id!!
        val result = gameService.fetchLogById(user, gameId)

        assertEquals(gameDAO.rounds[0].logMessages.size, result.size)
        assertEquals(gameDAO.rounds[0].logMessages[0], result[0])
        assertEquals(gameDAO.rounds[0].logMessages[1], result[1])
    }
}
