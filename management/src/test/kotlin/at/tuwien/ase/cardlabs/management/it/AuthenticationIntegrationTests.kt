package at.tuwien.ase.cardlabs.management.it

import at.tuwien.ase.cardlabs.management.TestHelper
import at.tuwien.ase.cardlabs.management.controller.model.Account
import at.tuwien.ase.cardlabs.management.database.repository.AccountRepository
import at.tuwien.ase.cardlabs.management.service.AccountService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuthenticationIntegrationTests {

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
    fun whenLogin_expectSuccess() {
        createAccount("test", "test@test.com", "password", null, true, true, true)

        val body = TestHelper.createAccountLoginJSON("test", "password")
        mockMvc.perform(
            post("/authentication/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isOk)
    }

    @Test
    fun whenLogin_withInvalidPassword_expectUnauthorizedError() {
        createAccount("test", "test@test.com", "password", null, true, true, true)

        val body = TestHelper.createAccountLoginJSON("test", "password2")
        mockMvc.perform(
            post("/authentication/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isUnauthorized)
    }

    private fun createAccount(username: String, email: String, password: String, location: String?, sendScoreUpdates: Boolean, sendChangeUpdates: Boolean, sendNewsletter: Boolean): Account {
        return TestHelper.createAccount(accountService, username, email, password, location, sendScoreUpdates, sendChangeUpdates, sendNewsletter)
    }
}
