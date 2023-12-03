package at.tuwien.ase.cardlabs.management.ut

import at.tuwien.ase.cardlabs.management.TestHelper
import at.tuwien.ase.cardlabs.management.controller.model.account.Account
import at.tuwien.ase.cardlabs.management.controller.model.bot.Bot
import at.tuwien.ase.cardlabs.management.controller.model.bot.BotCreate
import at.tuwien.ase.cardlabs.management.controller.model.bot.BotPatch
import at.tuwien.ase.cardlabs.management.database.model.bot.BotState
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.service.AccountService
import at.tuwien.ase.cardlabs.management.service.bot.BotService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
internal class BotServiceTests {

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var botService: BotService

    @Test
    fun whenBotCreate_withValidData_expectSuccess() {
        val account = createAccount("test", "test@test.com", "PassWord123?!")
        val user = TestHelper.createUserDetails(account)

        val botCreate = BotCreate(
            name = "Tytartron"
        )
        val result = assertDoesNotThrow {
            botService.create(user, botCreate)
        }

        assertNotNull(result)
        assertNotNull(result.id)
        assertEquals(botCreate.name, result.name)
        assertEquals(account.id!!, result.ownerId)
        assertEquals("", result.currentCode)
        assertEquals(0, result.codeHistory.size)
        assertEquals(1000, result.eloScore)
        assertEquals(BotState.CREATED, result.currentState)
        assertEquals(BotState.READY, result.defaultState)
    }

    @Test
    fun whenBotPatch_withNoData_expectSuccess() {
        val account = createAccount("test", "test@test.com", "PassWord123?!")
        val user = TestHelper.createUserDetails(account)
        val bot = createBot(user, "Tytartron", null)

        val botFetch = BotPatch()
        val result = assertDoesNotThrow {
            botService.patch(user, bot.id!!, botFetch)
        }
        assertEquals(bot.id, result.id)
        assertEquals(bot.name, result.name)
        assertEquals(bot.ownerId, result.ownerId)
        assertEquals("", result.currentCode)
        assertEquals(bot.codeHistory.size, result.codeHistory.size)
        assertEquals(bot.eloScore, result.eloScore)
        assertEquals(bot.currentState, result.currentState)
        assertEquals(bot.defaultState, result.defaultState)
        assertEquals(bot.errorStateMessage, result.errorStateMessage)
    }

    @Test
    fun whenBotPatch_withCodeData_expectSuccess() {
        val account = createAccount("test", "test@test.com", "PassWord123?!")
        val user = TestHelper.createUserDetails(account)
        val bot = createBot(user, "Tytartron", null)

        val botFetch = BotPatch(
            currentCode = "x = 5"
        )
        val result = assertDoesNotThrow {
            botService.patch(user, bot.id!!, botFetch)
        }
        assertEquals(bot.id, result.id)
        assertEquals(bot.name, result.name)
        assertEquals(bot.ownerId, result.ownerId)
        assertEquals(botFetch.currentCode, result.currentCode)
        assertEquals(bot.codeHistory.size, result.codeHistory.size)
        assertEquals(bot.eloScore, result.eloScore)
        assertEquals(bot.currentState, result.currentState)
        assertEquals(bot.defaultState, result.defaultState)
        assertEquals(bot.errorStateMessage, result.errorStateMessage)
    }

    @Test
    fun whenBotRank_withValidData_expectSuccess() {
        val account = createAccount("test", "test@test.com", "PassWord123?!")
        val user = TestHelper.createUserDetails(account)
        val bot = createBot(user, "Tytartron", "x = 5")

        val result = assertDoesNotThrow {
            botService.createCodeVersion(user, bot.id!!)
        }
    }

    @Test
    fun whenBotFetchById_withValidData_expectSuccess() {
        val account = createAccount("test", "test@test.com", "PassWord123?!")
        val userDetails = TestHelper.createUserDetails(account)
        val bot = createBot(userDetails, "Tytartron", null)

        val result = botService.fetch(userDetails, bot.id!!)
        assertNotNull(result)
        assertNotNull(result.id)
        assertEquals("Tytartron", result.name)
        assertEquals(account.id!!, result.ownerId)
        assertEquals("", result.currentCode)
        assertEquals(0, result.codeHistory.size)
        assertEquals(1000, result.eloScore)
        assertEquals(BotState.CREATED, result.currentState)
        assertEquals(BotState.READY, result.defaultState)
    }

    @Test
    fun whenBotFetchByAll_withValidData_expectSuccess() {
        val account = createAccount("test", "test@test.com", "PassWord123?!")
        val userDetails = TestHelper.createUserDetails(account)
        val bot1 = createBot(userDetails, "Tytartron", null)
        val bot2 = createBot(userDetails, "Neophotron", null)

        val pageable = PageRequest.of(0, 10)
        val result = botService.fetchAll(userDetails, pageable)
        assertNotNull(result)
        assertEquals(1, result.totalPages)
        assertEquals(2, result.totalElements)
    }

    @Test
    fun whenBotDelete_withValidData_expectSuccess() {
        val account = createAccount("test", "test@test.com", "PassWord123?!")
        val userDetails = TestHelper.createUserDetails(account)
        val bot = createBot(userDetails, "Tytartron", null)

        val result = assertDoesNotThrow {
            botService.delete(userDetails, bot.id!!)
        }
    }

    @Test
    fun whenBotRankPosition_withValidData_expectSuccess() {
        val account = createAccount("test", "test@test.com", "PassWord123?!")
        val userDetails = TestHelper.createUserDetails(account)
        val bot = createBot(userDetails, "Tytartron", null)

        val result = assertDoesNotThrow {
            botService.fetchRankPosition(userDetails, bot.id!!)
        }
        assertEquals(1, result)
    }

    private fun createAccount(username: String, email: String, password: String): Account {
        return TestHelper.createAccount(
            accountService = accountService,
            username = username,
            email = email,
            password = password,
            location = null,
            sendScoreUpdates = false,
            sendChangeUpdates = false,
            sendNewsletter = false
        )
    }

    private fun createBot(user: CardLabUser, name: String, code: String?): Bot {
        return TestHelper.createBot(botService, user, name, code)
    }
}
