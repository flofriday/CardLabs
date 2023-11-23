package at.tuwien.ase.cardlabs.management.it

import at.tuwien.ase.cardlabs.management.TestHelper
import at.tuwien.ase.cardlabs.management.controller.model.Account
import at.tuwien.ase.cardlabs.management.database.model.LocationDAO
import at.tuwien.ase.cardlabs.management.database.repository.AccountRepository
import at.tuwien.ase.cardlabs.management.database.repository.LocationRepository
import at.tuwien.ase.cardlabs.management.security.authentication.JwtAuthenticationResponse
import at.tuwien.ase.cardlabs.management.service.AccountService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AccountIntegrationTests {

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var locationRepository: LocationRepository

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var mockMvc: MockMvc
    private val countries: List<String> = listOf("Austria", "Germany", "Netherlands")

    @BeforeEach
    fun beforeEach() {
        accountRepository.deleteAll()
        locationRepository.deleteAll()
        for (country: String in countries) {
            val c = LocationDAO()
            c.name = country
            locationRepository.save(c)
        }
    }

    @Test
    fun whenAccountCreate_expectSuccess() {
        val body = TestHelper.createAccountCreateJSON("test", "test@test.com", "password", null, true, true, true)
        val result = mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isCreated)
            .andReturn()
        val jsonResponseString = result.response.contentAsString
        val response = jacksonObjectMapper().readValue<Account>(jsonResponseString)

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
    fun whenAccountCreateWithCountry_expectSuccess() {
        val body = TestHelper.createAccountCreateJSON("test", "test@test.com", "password", "Austria", true, true, true)
        val result = mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isCreated)
            .andReturn()
        val jsonResponseString = result.response.contentAsString
        val response = jacksonObjectMapper().readValue<Account>(jsonResponseString)

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
    fun whenAccountCreateWithSendScore_expectSuccessAndSendScoreTrue() {
        val body = TestHelper.createAccountCreateJSON("test", "test@test.com", "password", "Austria", true, false, false)
        val result = mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isCreated)
            .andReturn()
        val jsonResponseString = result.response.contentAsString
        val response = jacksonObjectMapper().readValue<Account>(jsonResponseString)

        assertEquals(true, response.sendScoreUpdates)
        assertEquals(false, response.sendChangeUpdates)
        assertEquals(false, response.sendNewsletter)
    }

    @Test
    fun whenAccountCreateWithSendUpdate_expectSuccessAndSendUpdatesTrue() {
        val body = TestHelper.createAccountCreateJSON("test", "test@test.com", "password", "Austria", false, true, false)
        val result = mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isCreated)
            .andReturn()
        val jsonResponseString = result.response.contentAsString
        val response = jacksonObjectMapper().readValue<Account>(jsonResponseString)

        assertEquals(false, response.sendScoreUpdates)
        assertEquals(true, response.sendChangeUpdates)
        assertEquals(false, response.sendNewsletter)
    }

    @Test
    fun whenAccountCreateWithSendNewsletter_expectSuccessAndSendNewsletterTrue() {
        val body = TestHelper.createAccountCreateJSON("test", "test@test.com", "password", "Austria", false, false, true)
        val result = mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isCreated)
            .andReturn()
        val jsonResponseString = result.response.contentAsString
        val response = jacksonObjectMapper().readValue<Account>(jsonResponseString)

        assertEquals(false, response.sendScoreUpdates)
        assertEquals(false, response.sendChangeUpdates)
        assertEquals(true, response.sendNewsletter)
    }

    @Test
    fun whenAccountCreateWithExistingUsername_expectAccountExistError() {
        createAccount("test", "test@test.com", "password", "Austria", true, true, true)

        val body = TestHelper.createAccountCreateJSON("test", "other@test.com", "password", null, true, true, true)
        mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isConflict)
    }

    @Test
    fun whenGetUserInfo_expectSuccess() {
        createAccount("test", "test@test.com", "password", null, true, true, true)
        val authenticationToken = getAuthenticationToken("test", "password")

        val result = mockMvc.perform(
            get("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $authenticationToken"),
        )
            .andExpect(status().isOk)
            .andReturn()
        val jsonResponseString = result.response.contentAsString
        val response = jacksonObjectMapper().readValue<Account>(jsonResponseString)

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
    fun whenGetUserInfoNotToken_expectForbidden() {
        createAccount("test", "test@test.com", "password", null, true, true, true)

        mockMvc.perform(
            get("/account")
                .contentType(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isForbidden)
    }

    @Test
    fun whenUpdateUser_expectSuccess() {
        createAccount("test", "test@test.com", "password", null, true, true, true)
        val authenticationToken = getAuthenticationToken("test", "password")
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
    fun whenUpdateUserWithInvalidLocation_expectBadRequest() {
        createAccount("test", "test@test.com", "password", null, true, true, true)
        val authenticationToken = getAuthenticationToken("test", "password")
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
    fun whenAccountCreateWithExistingEmail_expectAccountExistError() {
        createAccount("test", "test@test.com", "password", "Austria", true, true, true)

        val body = TestHelper.createAccountCreateJSON("other", "test@test.com", "password", null, true, true, true)
        mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isConflict)
    }

    @Test
    fun whenAccountCreateWithInvalidCountry_expectBadRequest() {
        val body = TestHelper.createAccountCreateJSON("test", "test@test.com", "password", "Non-Exist", true, true, true)
        mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun whenAccountDelete_expectSuccess() {
        val account = createAccount("test", "test@test.com", "password", null, true, true, true)
        val authenticationToken = getAuthenticationToken("test", "password")

        mockMvc.perform(
            delete("/account/${account.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $authenticationToken"),
        )
            .andExpect(status().isOk)
    }

    @Test
    fun whenAccountDelete_expectUnauthorizedError() {
        val account1 = createAccount("test", "test@test.com", "password", null, true, true, true)
        val account2 = createAccount("test2", "test2@test.com", "password", null, true, true, true)
        val authenticationToken = getAuthenticationToken("test", "password")

        mockMvc.perform(
            delete("/account/${account2.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $authenticationToken"),
        )
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun whenAccountCreate_expectIllegalArgumentError() {
        val body = """
            {
                "email": "test@test.com",
                "password": "password"
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
        val body = TestHelper.createAccountLoginJSON(username, password)
        val result = mockMvc.perform(
            post("/authentication/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isOk)
            .andReturn()
        val jsonResponseString = result.response.contentAsString
        val response = jacksonObjectMapper().readValue<JwtAuthenticationResponse>(jsonResponseString)
        return response.jwt
    }

    private fun createAccount(username: String, email: String, password: String, location: String?, sendScoreUpdates: Boolean, sendChangeUpdates: Boolean, sendNewsletter: Boolean): Account {
        return TestHelper.createAccount(accountService, username, email, password, location, sendScoreUpdates, sendChangeUpdates, sendNewsletter)
    }

    private fun getAccount(username: String): Account {
        return TestHelper.getAccount(accountService, username)
    }
}
