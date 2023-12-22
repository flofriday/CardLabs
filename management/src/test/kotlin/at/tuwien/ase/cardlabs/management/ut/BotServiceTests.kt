package at.tuwien.ase.cardlabs.management.ut

import at.tuwien.ase.cardlabs.management.TestConfig
import at.tuwien.ase.cardlabs.management.TestHelper
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
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
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

    @ParameterizedTest
    @CsvFileSource(resources = ["/service/bot/bot_service_create_test_parameter.csv"])
    fun testBotCreate(name: String, currentCode: String?, success: Boolean, errorMessage: String, description: String) {
        val account = TestHelper.createAccount(accountService)
        val user = TestHelper.createUserDetails(account)

        val botCreate = BotCreate(
            name = "Tytartron",
            currentCode = currentCode.takeUnless { it.equals(TestConfig.CSV_NULL_VALUE) },
        )

        if (success) {
            val result = assertDoesNotThrow {
                botService.create(user, botCreate)
            }

            assertNotNull(result)
            assertNotNull(result.id)
            assertEquals(botCreate.name, result.name)
            assertEquals(account.id!!, result.ownerId)
            assertEquals(if (currentCode.equals(TestConfig.CSV_NULL_VALUE)) "" else currentCode, result.currentCode)
            assertEquals(0, result.codeHistory.size)
            assertEquals(1000, result.eloScore)
            assertEquals(BotState.CREATED, result.currentState)
            assertEquals(BotState.READY, result.defaultState)
        } else {
            val exception = assertThrows<Exception> {
                botService.create(user, botCreate)
            }
            assertEquals(errorMessage, exception.message)
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/service/bot/bot_service_patch_test_parameter.csv"])
    fun testBotPatch(currentCode: String?, success: Boolean, errorMessage: String, description: String) {
        val account = TestHelper.createAccount(accountService)
        val user = TestHelper.createUserDetails(account)
        val bot = createBot(user, "Tytartron", null)

        val botFetch = BotPatch(
            currentCode = currentCode.takeUnless { it.equals(TestConfig.CSV_NULL_VALUE) },
        )

        if (success) {
            val result = assertDoesNotThrow {
                botService.patch(user, bot.id!!, botFetch)
            }
            assertEquals(bot.id, result.id)
            assertEquals(bot.name, result.name)
            assertEquals(bot.ownerId, result.ownerId)
            assertEquals(if (currentCode.equals(TestConfig.CSV_NULL_VALUE)) "" else currentCode, result.currentCode)
            assertEquals(bot.codeHistory.size, result.codeHistory.size)
            assertEquals(bot.eloScore, result.eloScore)
            assertEquals(bot.currentState, result.currentState)
            assertEquals(bot.defaultState, result.defaultState)
            assertEquals(bot.errorStateMessage, result.errorStateMessage)
        } else {
            val exception = assertThrows<Exception> {
                botService.patch(user, bot.id!!, botFetch)
            }
            assertEquals(errorMessage, exception.message)
        }
    }

    @Test
    fun whenBotCodeVersion_withValidData_expectSuccess() {
        val account = TestHelper.createAccount(accountService)
        val user = TestHelper.createUserDetails(account)
        val bot = createBot(user, "Tytartron", "x = 5")

        val result = assertDoesNotThrow {
            botService.createCodeVersion(user, bot.id!!)
        }
    }

    @Test
    fun whenBotFetchById_withValidData_expectSuccess() {
        val account = TestHelper.createAccount(accountService)
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
        val account = TestHelper.createAccount(accountService)
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
        val account = TestHelper.createAccount(accountService)
        val userDetails = TestHelper.createUserDetails(account)
        val bot = createBot(userDetails, "Tytartron", null)

        val result = assertDoesNotThrow {
            botService.delete(userDetails, bot.id!!)
        }
    }

    @Test
    fun whenBotRankPosition_withOneBot_expectSuccess() {
        val account = TestHelper.createAccount(accountService)
        val userDetails = TestHelper.createUserDetails(account)
        val bot = createBot(userDetails, "Tytartron", null)

        val result = assertDoesNotThrow {
            botService.fetchRankPosition(userDetails, bot.id!!)
        }
        assertEquals(1, result)
    }

    @Test
    fun whenBotRankPosition_withMultipleBots_expectSuccess() {
        val account = TestHelper.createAccount(accountService)
        val userDetails = TestHelper.createUserDetails(account)
        val bot1 = createBot(userDetails, "Tytartron", null)
        val bot2 = createBot(userDetails, "Neophotron", null)

        val result1 = assertDoesNotThrow {
            botService.fetchRankPosition(userDetails, bot1.id!!)
        }
        assertEquals(1, result1)
        val result2 = assertDoesNotThrow {
            botService.fetchRankPosition(userDetails, bot2.id!!)
        }
        assertEquals(1, result2)
    }

    private fun createBot(user: CardLabUser, name: String, code: String?): Bot {
        return TestHelper.createBot(botService, user, name, code)
    }
}
