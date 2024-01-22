package at.tuwien.ase.cardlabs.management.it

import at.tuwien.ase.cardlabs.management.TestHelper
import at.tuwien.ase.cardlabs.management.WebApplicationTest
import at.tuwien.ase.cardlabs.management.controller.model.account.Account
import at.tuwien.ase.cardlabs.management.database.model.LocationDAO
import at.tuwien.ase.cardlabs.management.database.repository.AccountRepository
import at.tuwien.ase.cardlabs.management.database.repository.LocationRepository
import at.tuwien.ase.cardlabs.management.security.jwt.JwtTokenService
import at.tuwien.ase.cardlabs.management.service.AccountService
import at.tuwien.ase.cardlabs.management.util.Continent
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebApplicationTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AccountIntegrationTests {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var jwtTokenService: JwtTokenService

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var locationRepository: LocationRepository

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val countries: List<Pair<String, Continent>> =
        listOf(Pair("Austria", Continent.EUROPE), Pair("Germany", Continent.EUROPE), Pair("Japan", Continent.EUROPE))

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
    fun whenGetUserInfo_withExistingAccount_expectSuccess() {
        createAccount("test", "test@test.com", null)
        val authenticationToken = getAuthenticationToken("test")

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
        assertEquals(null, response.location)
    }

    @Test
    fun whenGetUserInfo_withoutToken_expectForbidden() {
        createAccount("test", "test@test.com", null)

        mockMvc.perform(
            get("/account")
                .contentType(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isForbidden)
    }

    @Test
    fun whenUpdateUser_withValidUserData_expectSuccess() {
        createAccount("test", "test@test.com", null)
        val authenticationToken = getAuthenticationToken("test")
        val body = TestHelper.createAccountUpdateCreateJSON("Austria")

        mockMvc.perform(
            patch("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $authenticationToken")
                .content(body),
        )
            .andExpect(status().isOk)

        val account = getAccount("test")
        assertEquals("Austria", account.location)
    }

    @Test
    fun whenUpdateUser_withInvalidLocation_expectBadRequest() {
        createAccount("test", "test@test.com", null)
        val authenticationToken = getAuthenticationToken("test")
        val body = TestHelper.createAccountUpdateCreateJSON("not-valid")

        mockMvc.perform(
            patch("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $authenticationToken")
                .content(body),
        )
            .andExpect(status().isBadRequest)
    }


    @Test
    fun whenAccountDelete_withValidJWT_expectSuccess() {
        createAccount("test", "test@test.com", null)
        val authenticationToken = getAuthenticationToken("test")

        mockMvc.perform(
            delete("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $authenticationToken"),
        )
            .andExpect(status().isOk)
    }

    @Test
    fun whenAccountDelete_withoutJWT_expectForbiddenError() {
        createAccount("test", "test@test.com", null)
        getAuthenticationToken("test")

        mockMvc.perform(
            delete("/account")
                .contentType(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isForbidden)
    }


    private fun getAuthenticationToken(username: String): String {
        return TestHelper.getInitialAuthenticationTokens(jwtTokenService, accountRepository, username).accessToken.token
    }

    private fun createAccount(
        username: String,
        email: String,
        location: String?
    ): Account {
        return TestHelper.createAccount(
            accountService,
            username,
            email,
            location
        )
    }

    private fun getAccount(username: String): Account {
        return TestHelper.getAccount(accountService, username)
    }
}
