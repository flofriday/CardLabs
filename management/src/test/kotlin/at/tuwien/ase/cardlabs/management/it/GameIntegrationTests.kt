package at.tuwien.ase.cardlabs.management.it

import at.tuwien.ase.cardlabs.management.TestHelper
import at.tuwien.ase.cardlabs.management.controller.model.game.Game
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
import at.tuwien.ase.cardlabs.management.service.AccountService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GameIntegrationTests {

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
    fun whenGameFetchLogById_withExistingGame_expectSuccess() {
        val account = TestHelper.createAccount(accountService)
        val jwt = TestHelper.getAuthenticationToken(mockMvc, account.username, TestHelper.DEFAULT_PASSWORD)
        val botId = 0L
        val botCodeId = 0L

        val gameDAO = GameDAO()
        gameDAO.startTime = LocalDateTime.of(2023, 12, 11, 15, 0)
        gameDAO.endTime = LocalDateTime.of(2023, 12, 11, 16, 0)
        gameDAO.actions = listOf(
            Action(botId, ActionType.INITIAL_CARD_DRAW, Card(CardType.DRAW_TWO, Color.RED, null), null),
            Action(botId, ActionType.DRAW_CARD, Card(CardType.NUMBER_CARD, Color.BLUE, 5), null),
        )
        gameDAO.results = listOf(
            Result(botId, botCodeId, 1000, 1015, 0),
        )
        gameDAO.logMessages = listOf(
            SystemLogMessage(0, "Test message for system"),
            DebugLogMessage(0, "Test message for debug", botId),
        )
        val gameId = gameRepository.save(gameDAO).id!!

        val result = mockMvc.perform(
            get("/match/$gameId/all")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $jwt"),
        )
            .andExpect(status().isOk)
            .andReturn()
        val response = jacksonObjectMapper().readValue<Game>(result.response.contentAsString)

        assertEquals(gameDAO.startTime, response.startTime)
        assertEquals(gameDAO.endTime, response.endTime)
        assertEquals(gameDAO.actions.size, response.actions.size)
        assertEquals(gameDAO.actions[0], response.actions[0])
        assertEquals(gameDAO.actions[1], response.actions[1])
        assertEquals(gameDAO.results.size, response.results.size)
        assertEquals(gameDAO.results[0], response.results[0])
        assertEquals(gameDAO.logMessages.size, response.logMessages.size)
        assertEquals(gameDAO.logMessages[0], response.logMessages[0])
        assertEquals(gameDAO.logMessages[1], response.logMessages[1])
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
}
