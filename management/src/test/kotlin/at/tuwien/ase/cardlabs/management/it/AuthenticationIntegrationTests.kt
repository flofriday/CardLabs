package at.tuwien.ase.cardlabs.management.it

import at.tuwien.ase.cardlabs.management.TestHelper
import at.tuwien.ase.cardlabs.management.WebApplicationTest
import at.tuwien.ase.cardlabs.management.controller.model.account.Account
import at.tuwien.ase.cardlabs.management.service.AccountService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebApplicationTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthenticationIntegrationTests {

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun whenLogin_expectSuccess() {
        createAccount("test", "test@test.com", null)

        val body = TestHelper.createAccountLoginJSON("test")
        mockMvc.perform(
            post("/authentication/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isOk)
    }

    @Test
    fun whenLogin_withInvalidPassword_expectUnauthorizedError() {
        createAccount("test", "test@test.com", null)

        val body = TestHelper.createAccountLoginJSON("test")
        mockMvc.perform(
            post("/authentication/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(status().isUnauthorized)
    }

    private fun createAccount(username: String, email: String, location: String?): Account {
        return TestHelper.createAccount(accountService, username, email, location)
    }
}
