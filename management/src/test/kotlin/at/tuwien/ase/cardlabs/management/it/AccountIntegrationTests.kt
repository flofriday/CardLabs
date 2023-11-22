package at.tuwien.ase.cardlabs.management.it

import at.tuwien.ase.cardlabs.management.TestHelper
import at.tuwien.ase.cardlabs.management.controller.model.Account
import at.tuwien.ase.cardlabs.management.database.repository.AccountRepository
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AccountIntegrationTests {

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun beforeEach() {
        accountRepository.deleteAll()
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
    }

    @Test
    fun whenAccountCreate_expectAccountExistError() {
        createAccount("test", "test@test.com", "password", "Austria", true, true, true)

        val body = TestHelper.createAccountCreateJSON("test", "test@test.com", "password", null, true, true, true)
        mockMvc.perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isConflict)
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
}
