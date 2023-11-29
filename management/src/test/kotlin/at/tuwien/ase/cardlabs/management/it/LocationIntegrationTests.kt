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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class LocationIntegrationTests {

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var locationRepository: LocationRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var accountService: AccountService

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
    fun whenGetLocations_withoutJWT_expectSuccess() {
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/locations"),
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val jsonResponseString = result.response.contentAsString
        val response = jacksonObjectMapper().readValue<List<String>>(jsonResponseString)
        assertEquals(countries, response)
    }

    @Test
    fun whenGetLocations_withValidJWT_expectSuccess() {
        createAccount("test", "test@test.com", "password", null, true, true, true)
        val authenticationToken = getAuthenticationToken("test", "password")

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/locations")
                .header("Authorization", "Bearer $authenticationToken"),
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val jsonResponseString = result.response.contentAsString
        val response = jacksonObjectMapper().readValue<List<String>>(jsonResponseString)
        assertEquals(countries, response)
    }

    private fun getAuthenticationToken(username: String, password: String): String {
        val body = TestHelper.createAccountLoginJSON(username, password)
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/authentication/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val jsonResponseString = result.response.contentAsString
        val response = jacksonObjectMapper().readValue<JwtAuthenticationResponse>(jsonResponseString)
        return response.jwt
    }

    private fun createAccount(username: String, email: String, password: String, location: String?, sendScoreUpdates: Boolean, sendChangeUpdates: Boolean, sendNewsletter: Boolean): Account {
        return TestHelper.createAccount(accountService, username, email, password, location, sendScoreUpdates, sendChangeUpdates, sendNewsletter)
    }
}
