package at.tuwien.ase.cardlabs.management.it

import at.tuwien.ase.cardlabs.management.TestHelper
import at.tuwien.ase.cardlabs.management.WebApplicationTest
import at.tuwien.ase.cardlabs.management.controller.model.game.Game
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
import at.tuwien.ase.cardlabs.management.database.model.game.round.Turn
import at.tuwien.ase.cardlabs.management.database.repository.GameRepository
import at.tuwien.ase.cardlabs.management.service.AccountService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@WebApplicationTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GameIntegrationTests {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var gameRepository: GameRepository

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun whenGameFetchAllById_widthNonExistingGame_expectGameDoesNotExistError() {
        val account = TestHelper.createAccount(accountService)
        val jwt = TestHelper.getAuthenticationToken(mockMvc, account.username, TestHelper.DEFAULT_PASSWORD)
        val gameId = 0L
        mockMvc.perform(
            get("/match/$gameId/all")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $jwt"),
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun whenGameFetchAllById_withExistingGame_expectSuccess() {
        val account = TestHelper.createAccount(accountService)
        val jwt = TestHelper.getAuthenticationToken(mockMvc, account.username, TestHelper.DEFAULT_PASSWORD)
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
        gameDAO.turns = listOf(
            Turn(0, topCard, drawPile, hand, actions, logMessages),
        )
        gameDAO.gameState = GameState.CREATED
        val gameId = gameRepository.save(gameDAO).id!!

        val result = mockMvc.perform(
            get("/match/$gameId/all")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $jwt"),
        )
            .andExpect(status().isOk)
            .andReturn()
        val response = objectMapper.readValue<Game>(result.response.contentAsString)

        assertEquals(gameDAO.startTime, response.startTime)
        assertEquals(gameDAO.endTime, response.endTime)
        assertEquals(gameDAO.winningBotId, response.winningBotId)
        assertEquals(gameDAO.turns.size, response.turns.size)
        assertEquals(gameDAO.turns[0].roundId, response.turns[0].roundId)
        assertEquals(gameDAO.turns[0].topCard, response.turns[0].topCard)
        assertEquals(gameDAO.turns[0].drawPile.size, response.turns[0].drawPile.size)
        assertEquals(gameDAO.turns[0].drawPile[0], response.turns[0].drawPile[0])
        assertEquals(gameDAO.turns[0].hands.size, response.turns[0].hands.size)
        assertEquals(gameDAO.turns[0].hands[0], response.turns[0].hands[0])
        assertEquals(gameDAO.turns[0].actions.size, response.turns[0].actions.size)
        assertEquals(gameDAO.turns[0].actions[0], response.turns[0].actions[0])
        assertEquals(gameDAO.turns[0].logMessages.size, response.turns[0].logMessages.size)
        assertEquals(gameDAO.turns[0].logMessages[0], response.turns[0].logMessages[0])
        assertEquals(gameDAO.turns[0].logMessages[1], response.turns[0].logMessages[1])
        assertEquals(gameDAO.gameState, response.gameState)
    }

    @Test
    fun whenGameFetchLogById_widthNonExistingGame_expectGameDoesNotExistError() {
        val account = TestHelper.createAccount(accountService)
        val jwt = TestHelper.getAuthenticationToken(mockMvc, account.username, TestHelper.DEFAULT_PASSWORD)
        val gameId = 0L
        mockMvc.perform(
            get("/match/$gameId/all")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $jwt"),
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun whenGameFetchLogById_withExistingGame_expectSuccess() {
        val account = TestHelper.createAccount(accountService)
        val jwt = TestHelper.getAuthenticationToken(mockMvc, account.username, TestHelper.DEFAULT_PASSWORD)
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
        gameDAO.turns = listOf(
            Turn(0, topCard, emptyList(), emptyList(), emptyList(), logMessages),
        )
        gameDAO.gameState = GameState.CREATED
        val gameId = gameRepository.save(gameDAO).id!!

        val result = mockMvc.perform(
            get("/match/$gameId/all")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $jwt"),
        )
            .andExpect(status().isOk)
            .andReturn()
        val response = objectMapper.readValue<Game>(result.response.contentAsString)

        assertEquals(gameDAO.startTime, response.startTime)
        assertEquals(gameDAO.endTime, response.endTime)
        assertEquals(gameDAO.winningBotId, response.winningBotId)
        assertEquals(gameDAO.turns.size, response.turns.size)
        assertEquals(gameDAO.turns[0].roundId, response.turns[0].roundId)
        assertEquals(gameDAO.turns[0].topCard, response.turns[0].topCard)
        assertEquals(gameDAO.turns[0].logMessages.size, response.turns[0].logMessages.size)
        assertEquals(gameDAO.turns[0].logMessages[0], response.turns[0].logMessages[0])
        assertEquals(gameDAO.turns[0].logMessages[1], response.turns[0].logMessages[1])
        assertEquals(gameDAO.gameState, response.gameState)
    }
}
