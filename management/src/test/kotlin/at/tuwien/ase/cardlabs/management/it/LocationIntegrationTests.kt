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
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebApplicationTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LocationIntegrationTests {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var locationRepository: LocationRepository

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var jwtTokenService: JwtTokenService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var accountService: AccountService

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
    fun whenGetLocations_withoutJWT_expectSuccess() {
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/locations"),
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val jsonResponseString = result.response.contentAsString
        val response = jacksonObjectMapper().readValue<List<String>>(jsonResponseString)
        assertEquals(countries.stream().map { f -> f.first }.toList(), response)
    }

    @Test
    fun whenGetLocations_withValidJWT_expectSuccess() {
        createAccount("test", "test@test.com", null)
        val authenticationTokens = TestHelper.getInitialAuthenticationTokens(jwtTokenService, accountRepository, "test")

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/locations")
                .header("Authorization", "Bearer ${authenticationTokens.accessToken.token}"),
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val jsonResponseString = result.response.contentAsString
        val response = jacksonObjectMapper().readValue<List<String>>(jsonResponseString)
        assertEquals(countries.stream().map { f -> f.first }.toList(), response)
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
}
