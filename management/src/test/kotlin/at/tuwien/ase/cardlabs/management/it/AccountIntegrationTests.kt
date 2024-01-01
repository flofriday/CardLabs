package at.tuwien.ase.cardlabs.management.it

import at.tuwien.ase.cardlabs.management.TestHelper
import at.tuwien.ase.cardlabs.management.WebApplicationTest
import at.tuwien.ase.cardlabs.management.controller.model.account.Account
import at.tuwien.ase.cardlabs.management.database.model.LocationDAO
import at.tuwien.ase.cardlabs.management.database.repository.AccountRepository
import at.tuwien.ase.cardlabs.management.database.repository.LocationRepository
import at.tuwien.ase.cardlabs.management.service.AccountService
import at.tuwien.ase.cardlabs.management.util.Continent
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebApplicationTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AccountIntegrationTests {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var locationRepository: LocationRepository

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val countries: List<Pair<String, Continent>> = listOf(Pair("Austria", Continent.EUROPE), Pair("Germany", Continent.EUROPE), Pair("Japan", Continent.EUROPE))

    @BeforeEach
    fun beforeEach() {
        for (country: Pair<String, Continent> in countries) {
            val c = LocationDAO()
            c.name = country.first
            c.continent = country.second
            locationRepository.save(c)
        }
    }

    @Test
    fun whenAccountCreate_withValidAccountDataAndNullLocation_expectSuccess() {
        val body = TestHelper.createAccountCreateJSON("test", "test@test.com", "PassWord1!?", null, true, true, true)
        val result = mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isCreated)
            .andReturn()
        val jsonResponseString = result.response.contentAsString
        val response = objectMapper.readValue<Account>(jsonResponseString)

        assertNotNull(response.id)
        assertEquals("test", response.username)
        assertEquals("test@test.com", response.email)
        assertEquals("REDACTED", response.password)
        assertEquals(null, response.location)
        assertEquals(true, response.sendScoreUpdates)
        assertEquals(true, response.sendChangeUpdates)
        assertEquals(true, response.sendNewsletter)
    }

    @Test
    fun whenAccountCreate_withValidAccountDataAndNotNullLocation_expectSuccess() {
        val body =
            TestHelper.createAccountCreateJSON("test", "test@test.com", "PassWord1!?", "Austria", true, true, true)
        val result = mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isCreated)
            .andReturn()
        val jsonResponseString = result.response.contentAsString
        val response = objectMapper.readValue<Account>(jsonResponseString)

        assertNotNull(response.id)
        assertEquals("test", response.username)
        assertEquals("test@test.com", response.email)
        assertEquals("REDACTED", response.password)
        assertEquals("Austria", response.location)
        assertEquals(true, response.sendScoreUpdates)
        assertEquals(true, response.sendChangeUpdates)
        assertEquals(true, response.sendNewsletter)
    }

    @Test
    fun whenAccountCreate_withValidAccountAndSendScore_expectSuccessAndSendScoreTrue() {
        val body =
            TestHelper.createAccountCreateJSON("test", "test@test.com", "PassWord1!?", "Austria", true, false, false)
        val result = mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isCreated)
            .andReturn()
        val jsonResponseString = result.response.contentAsString
        val response = objectMapper.readValue<Account>(jsonResponseString)

        assertEquals(true, response.sendScoreUpdates)
        assertEquals(false, response.sendChangeUpdates)
        assertEquals(false, response.sendNewsletter)
    }

    @Test
    fun whenAccountCreate_withValidAccountAndSendUpdate_expectSuccessAndSendUpdatesTrue() {
        val body =
            TestHelper.createAccountCreateJSON("test", "test@test.com", "PassWord1!?", "Austria", false, true, false)
        val result = mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isCreated)
            .andReturn()
        val jsonResponseString = result.response.contentAsString
        val response = objectMapper.readValue<Account>(jsonResponseString)

        assertEquals(false, response.sendScoreUpdates)
        assertEquals(true, response.sendChangeUpdates)
        assertEquals(false, response.sendNewsletter)
    }

    @Test
    fun whenAccountCreate_withToShortPassword_expectBadRequest() {
        val body = TestHelper.createAccountCreateJSON("test", "test@test.com", "Pw12?!", "Austria", false, true, false)
        mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun whenAccountCreate_withPasswordThatContainsWhitespaces_expectBadRequest() {
        val body =
            TestHelper.createAccountCreateJSON("test", "test@test.com", "Password12?! ", "Austria", false, true, false)
        mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun whenAccountCreate_withPasswordThatContainsNoDigit_expectBadRequest() {
        val body =
            TestHelper.createAccountCreateJSON("test", "test@test.com", "Password?!", "Austria", false, true, false)
        mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun whenAccountCreate_withPasswordThatContainsNoUpperCaseChar_expectBadRequest() {
        val body =
            TestHelper.createAccountCreateJSON("test", "test@test.com", "password213?!", "Austria", false, true, false)
        mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun whenAccountCreate_withPasswordThatContainsNoLowerCaseChar_expectBadRequest() {
        val body =
            TestHelper.createAccountCreateJSON("test", "test@test.com", "PASSWORD213?!", "Austria", false, true, false)
        mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun whenAccountCreate_withInvalidEmail1_expectBadRequest() {
        val body =
            TestHelper.createAccountCreateJSON("test", "test@test", "PassWord123?!", "Austria", false, true, false)
        mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun whenAccountCreate_withInvalidEmail2_expectBadRequest() {
        val body =
            TestHelper.createAccountCreateJSON("test", "test@test.", "PassWord123?!", "Austria", false, true, false)
        mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun whenAccountCreate_withInvalidEmail3_expectBadRequest() {
        val body =
            TestHelper.createAccountCreateJSON("test", ".test@test.com", "PassWord123?!", "Austria", false, true, false)
        mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun whenAccountCreate_withInvalidEmail4_expectBadRequest() {
        val body = TestHelper.createAccountCreateJSON(
            "test",
            "test@test@test.com",
            "PassWord123?!",
            "Austria",
            false,
            true,
            false,
        )
        mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun whenAccountCreate_withEmptyEmail_expectBadRequest() {
        val body = TestHelper.createAccountCreateJSON("test", "", "PassWord123?!", "Austria", false, true, false)
        mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun whenAccountCreate_withUsernameThatContainsWhitespaces_expectBadRequest() {
        val body =
            TestHelper.createAccountCreateJSON("test ", "test@test.com", "PassWord123?!", "Austria", false, true, false)
        mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun whenAccountCreate_withEmptyUsername_expectBadRequest() {
        val body =
            TestHelper.createAccountCreateJSON("", "test@test.com", "PassWord123?!", "Austria", false, true, false)
        mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun whenAccountCreate_withPasswordThatContainsNoSpecialChar_expectBadRequest() {
        val body =
            TestHelper.createAccountCreateJSON("test", "test@test.com", "password213", "Austria", false, true, false)
        mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun whenAccountCreate_withValidAccountAndSendNewsletter_expectSuccessAndSendNewsletterTrue() {
        val body =
            TestHelper.createAccountCreateJSON("test", "test@test.com", "PassWord1!?", "Austria", false, false, true)
        val result = mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isCreated)
            .andReturn()
        val jsonResponseString = result.response.contentAsString
        val response = objectMapper.readValue<Account>(jsonResponseString)

        assertEquals(false, response.sendScoreUpdates)
        assertEquals(false, response.sendChangeUpdates)
        assertEquals(true, response.sendNewsletter)
    }

    @Test
    fun whenAccountCreate_withExistingUsername_expectAccountExistError() {
        createAccount("test", "test@test.com", "PassWord1!?", "Austria", true, true, true)

        val body = TestHelper.createAccountCreateJSON("test", "other@test.com", "PassWord1!?", null, true, true, true)
        mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isConflict)
    }

    @Test
    fun whenGetUserInfo_withExistingAccount_expectSuccess() {
        createAccount("test", "test@test.com", "PassWord1!?", null, true, true, true)
        val authenticationToken = getAuthenticationToken("test", "PassWord1!?")

        val result = mockMvc.perform(
            get("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $authenticationToken"),
        )
            .andExpect(status().isOk)
            .andReturn()
        val jsonResponseString = result.response.contentAsString
        val response = objectMapper.readValue<Account>(jsonResponseString)

        assertNotNull(response.id)
        assertEquals("test", response.username)
        assertEquals("test@test.com", response.email)
        assertEquals("REDACTED", response.password)
        assertEquals(null, response.location)
        assertEquals(true, response.sendScoreUpdates)
        assertEquals(true, response.sendChangeUpdates)
        assertEquals(true, response.sendNewsletter)
    }

    @Test
    fun whenGetUserInfo_withoutToken_expectForbidden() {
        createAccount("test", "test@test.com", "PassWord1!?", null, true, true, true)

        mockMvc.perform(
            get("/account")
                .contentType(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isForbidden)
    }

    @Test
    fun whenUpdateUser_withValidUserData_expectSuccess() {
        createAccount("test", "test@test.com", "PassWord1!?", null, true, true, true)
        val authenticationToken = getAuthenticationToken("test", "PassWord1!?")
        val body = TestHelper.createAccountUpdateCreateJSON("Austria", false, false, false)

        mockMvc.perform(
            patch("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $authenticationToken")
                .content(body),
        )
            .andExpect(status().isOk)

        val account = getAccount("test")
        assertEquals("Austria", account.location)
        assertEquals(false, account.sendNewsletter)
        assertEquals(false, account.sendChangeUpdates)
        assertEquals(false, account.sendScoreUpdates)
    }

    @Test
    fun whenUpdateUser_withInvalidLocation_expectBadRequest() {
        createAccount("test", "test@test.com", "PassWord1!?", null, true, true, true)
        val authenticationToken = getAuthenticationToken("test", "PassWord1!?")
        val body = TestHelper.createAccountUpdateCreateJSON("not-valid", false, false, false)

        mockMvc.perform(
            patch("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $authenticationToken")
                .content(body),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun whenAccountCreate_withExistingEmail_expectAccountExistError() {
        createAccount("test", "test@test.com", "PassWord1!?", "Austria", true, true, true)

        val body = TestHelper.createAccountCreateJSON("other", "test@test.com", "PassWord1!?", null, true, true, true)
        mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isConflict)
    }

    @Test
    fun whenAccountCreate_withInvalidCountry_expectBadRequest() {
        val body =
            TestHelper.createAccountCreateJSON("test", "test@test.com", "PassWord1!?", "Non-Exist", true, true, true)
        mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun whenAccountDelete_withValidJWT_expectSuccess() {
        createAccount("test", "test@test.com", "PassWord1!?", null, true, true, true)
        val authenticationToken = getAuthenticationToken("test", "PassWord1!?")

        mockMvc.perform(
            delete("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $authenticationToken"),
        )
            .andExpect(status().isOk)
    }

    @Test
    fun whenAccountDelete_withoutJWT_expectForbiddenError() {
        createAccount("test", "test@test.com", "PassWord1!?", null, true, true, true)
        getAuthenticationToken("test", "PassWord1!?")

        mockMvc.perform(
            delete("/account")
                .contentType(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isForbidden)
    }

    @Test
    fun whenAccountCreate_withoutUsername_expectIllegalArgumentError() {
        val body = """
            {
                "email": "test@test.com",
                "password": "PassWord1!?"
            }
        """
        mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isBadRequest)
    }

    private fun getAuthenticationToken(username: String, password: String): String {
        return TestHelper.getAuthenticationToken(mockMvc, username, password)
    }

    private fun createAccount(
        username: String,
        email: String,
        password: String,
        location: String?,
        sendScoreUpdates: Boolean,
        sendChangeUpdates: Boolean,
        sendNewsletter: Boolean,
    ): Account {
        return TestHelper.createAccount(
            accountService,
            username,
            email,
            password,
            location,
            sendScoreUpdates,
            sendChangeUpdates,
            sendNewsletter,
        )
    }

    private fun getAccount(username: String): Account {
        return TestHelper.getAccount(accountService, username)
    }
}
