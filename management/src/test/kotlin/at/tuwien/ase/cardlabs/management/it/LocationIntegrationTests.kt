package at.tuwien.ase.cardlabs.management.it

import at.tuwien.ase.cardlabs.management.database.model.LocationDAO
import at.tuwien.ase.cardlabs.management.database.repository.LocationRepository
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class LocationIntegrationTests {

    @Autowired
    private lateinit var locationRepository: LocationRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val countries: List<String> = listOf("Austria", "Germany", "Netherlands")

    @BeforeEach
    fun beforeEach() {
        locationRepository.deleteAll()
        for (country: String in countries) {
            val c = LocationDAO()
            c.name = country
            locationRepository.save(c)
        }
    }

    @Test
    fun whenGetLocations_expectSuccess() {
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/location"),
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val jsonResponseString = result.response.contentAsString
        val response = jacksonObjectMapper().readValue<List<String>>(jsonResponseString)
        assertEquals(countries, response)
    }
}
