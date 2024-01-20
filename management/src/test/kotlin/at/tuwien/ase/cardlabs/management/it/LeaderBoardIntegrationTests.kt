package at.tuwien.ase.cardlabs.management.it

import at.tuwien.ase.cardlabs.management.TestHelper
import at.tuwien.ase.cardlabs.management.WebApplicationTest
import at.tuwien.ase.cardlabs.management.controller.model.LeaderBoardEntry
import at.tuwien.ase.cardlabs.management.database.model.AccountDAO
import at.tuwien.ase.cardlabs.management.database.model.LocationDAO
import at.tuwien.ase.cardlabs.management.database.model.bot.BotDAO
import at.tuwien.ase.cardlabs.management.database.model.bot.BotState
import at.tuwien.ase.cardlabs.management.database.repository.AccountRepository
import at.tuwien.ase.cardlabs.management.database.repository.BotRepository
import at.tuwien.ase.cardlabs.management.database.repository.LocationRepository
import at.tuwien.ase.cardlabs.management.util.Continent
import at.tuwien.ase.cardlabs.management.util.RestResponsePage
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebApplicationTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LeaderBoardIntegrationTests {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var locationRepository: LocationRepository

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var botRepository: BotRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val countries: List<Pair<String, Continent>> =
        listOf(Pair("Austria", Continent.EUROPE), Pair("Germany", Continent.EUROPE), Pair("Japan", Continent.ASIA))
    private val globalViewBotNameList: ArrayList<String> = arrayListOf("Test12", "Test6", "Test11", "Test5", "Test10")
    private val continentViewAccount1Europe: ArrayList<String> =
        arrayListOf("Test6", "Test5", "Test4", "Test3", "Test2")

    @BeforeEach
    fun beforeEach() {
        val locations = ArrayList<LocationDAO>()

        for (country: Pair<String, Continent> in countries) {
            val c = LocationDAO()
            c.name = country.first
            c.continent = country.second
            locationRepository.save(c)
            locations.add(c)
        }

        val account1 = AccountDAO()
        account1.username = "Test1"
        account1.email = "test1@email.com"
        account1.password = passwordEncoder.encode("ThisIsATest1234")
        account1.location = locations[0]
        account1.sendChangeUpdates = true
        account1.sendScoreUpdates = true
        account1.sendNewsletter = true
        accountRepository.save(account1)

        val account2 = AccountDAO()
        account2.username = "Test2"
        account2.email = "test2@email.com"
        account2.password = passwordEncoder.encode("ThisIsASecondTest1234")
        account2.location = locations[2]
        account2.sendChangeUpdates = true
        account2.sendScoreUpdates = true
        account2.sendNewsletter = true
        accountRepository.save(account2)

        val bot1 = BotDAO()
        bot1.name = "Test1"
        bot1.owner = account1
        bot1.currentCode = "Test1 code"
        bot1.codeHistory = mutableListOf()
        bot1.eloScore = 1000
        bot1.currentState = BotState.CREATED
        bot1.defaultState = BotState.READY
        botRepository.save(bot1)

        val bot2 = BotDAO()
        bot2.name = "Test2"
        bot2.owner = account1
        bot2.currentCode = "Test2 code"
        bot2.codeHistory = mutableListOf()
        bot2.eloScore = 2000
        bot2.currentState = BotState.CREATED
        bot2.defaultState = BotState.READY
        botRepository.save(bot2)

        val bot3 = BotDAO()
        bot3.name = "Test3"
        bot3.owner = account1
        bot3.currentCode = "Test3 code"
        bot3.codeHistory = mutableListOf()
        bot3.eloScore = 3000
        bot3.currentState = BotState.CREATED
        bot3.defaultState = BotState.READY
        botRepository.save(bot3)

        val bot4 = BotDAO()
        bot4.name = "Test4"
        bot4.owner = account1
        bot4.currentCode = "Test4 code"
        bot4.codeHistory = mutableListOf()
        bot4.eloScore = 4000
        bot4.currentState = BotState.CREATED
        bot4.defaultState = BotState.READY
        botRepository.save(bot4)

        val bot5 = BotDAO()
        bot5.name = "Test5"
        bot5.owner = account1
        bot5.currentCode = "Test5 code"
        bot5.codeHistory = mutableListOf()
        bot5.eloScore = 5000
        bot5.currentState = BotState.CREATED
        bot5.defaultState = BotState.READY
        botRepository.save(bot5)

        val bot6 = BotDAO()
        bot6.name = "Test6"
        bot6.owner = account1
        bot6.currentCode = "Test6 code"
        bot6.codeHistory = mutableListOf()
        bot6.eloScore = 6000
        bot6.currentState = BotState.CREATED
        bot6.defaultState = BotState.READY
        botRepository.save(bot6)

        val bot7 = BotDAO()
        bot7.name = "Test7"
        bot7.owner = account2
        bot7.currentCode = "Test7 code"
        bot7.codeHistory = mutableListOf()
        bot7.eloScore = 1111
        bot7.currentState = BotState.CREATED
        bot7.defaultState = BotState.READY
        botRepository.save(bot7)

        val bot8 = BotDAO()
        bot8.name = "Test8"
        bot8.owner = account2
        bot8.currentCode = "Test8 code"
        bot8.codeHistory = mutableListOf()
        bot8.eloScore = 2222
        bot8.currentState = BotState.CREATED
        bot8.defaultState = BotState.READY
        botRepository.save(bot8)

        val bot9 = BotDAO()
        bot9.name = "Test9"
        bot9.owner = account2
        bot9.currentCode = "Test9 code"
        bot9.codeHistory = mutableListOf()
        bot9.eloScore = 3333
        bot9.currentState = BotState.CREATED
        bot9.defaultState = BotState.READY
        botRepository.save(bot9)

        val bot10 = BotDAO()
        bot10.name = "Test10"
        bot10.owner = account2
        bot10.currentCode = "Test10 code"
        bot10.codeHistory = mutableListOf()
        bot10.eloScore = 4444
        bot10.currentState = BotState.CREATED
        bot10.defaultState = BotState.READY
        botRepository.save(bot10)

        val bot11 = BotDAO()
        bot11.name = "Test11"
        bot11.owner = account2
        bot11.currentCode = "Test11 code"
        bot11.codeHistory = mutableListOf()
        bot11.eloScore = 5555
        bot11.currentState = BotState.CREATED
        bot11.defaultState = BotState.READY
        botRepository.save(bot11)

        val bot12 = BotDAO()
        bot12.name = "Test12"
        bot12.owner = account2
        bot12.currentCode = "Test12 code"
        bot12.codeHistory = mutableListOf()
        bot12.eloScore = 6666
        bot12.currentState = BotState.CREATED
        bot12.defaultState = BotState.READY
        botRepository.save(bot12)
    }

    @Test
    fun whenGetPublicLeaderboardGlobalView_withoutJWT_expectGlobalViewPage() {
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/leaderboard/public?page=0&entriesPerPage=5&region=GLOBAL"),
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val jsonResponseString = result.response.contentAsString
        val response = objectMapper.readValue<RestResponsePage<LeaderBoardEntry>>(jsonResponseString)
        val retrievedBotNames = response.content.map { it.botName }

        assertTrue(
            retrievedBotNames.containsAll(globalViewBotNameList),
            "Not all expected bot names are present in the response"
        )
        assertTrue(
            retrievedBotNames == globalViewBotNameList,
            "Bot names order in the response doesn't match the expected order"
        )
    }

    @Test
    fun whenGetPublicLeaderboardContinentView_withoutJWT_expect400() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/leaderboard/public?page=0&entriesPerPage=5&region=CONTINENT"),
        ).andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun whenGetPublicLeaderboardCountryView_withoutJWT_expect400() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/leaderboard/public?page=0&entriesPerPage=5&region=COUNTRY"),
        ).andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun whenGetPublicLeaderboardGlobalView_withJWT_expectGlobalViewPage() {
        val authenticationToken = getAuthenticationToken("Test1", "ThisIsATest1234")

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/leaderboard/public?page=0&entriesPerPage=5&region=GLOBAL")
                .header("Authorization", "Bearer $authenticationToken"),
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val jsonResponseString = result.response.contentAsString
        val response = objectMapper.readValue<RestResponsePage<LeaderBoardEntry>>(jsonResponseString)
        val retrievedBotNames = response.content.map { it.botName }

        assertTrue(
            retrievedBotNames.containsAll(globalViewBotNameList),
            "Not all expected bot names are present in the response"
        )
        assertTrue(
            retrievedBotNames == globalViewBotNameList,
            "Bot names order in the response doesn't match the expected order"
        )
    }

    @Test
    fun whenGetPublicLeaderboardContinentView_withJWT_withUserContinentSet_expectUserContinentPage() {
        val authenticationToken = getAuthenticationToken("Test1", "ThisIsATest1234")

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/leaderboard/public?page=0&entriesPerPage=5&region=CONTINENT&filter=Austria")
                .header("Authorization", "Bearer $authenticationToken"),
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val jsonResponseString = result.response.contentAsString
        val response = objectMapper.readValue<RestResponsePage<LeaderBoardEntry>>(jsonResponseString)
        val retrievedBotNames = response.content.map { it.botName }

        assertTrue(
            retrievedBotNames.containsAll(continentViewAccount1Europe),
            "Not all expected bot names are present in the response"
        )
        assertTrue(
            retrievedBotNames == continentViewAccount1Europe,
            "Bot names order in the response doesn't match the expected order"
        )
    }

    @Test
    fun whenGetPublicLeaderboardCountryView_withJWT_withUserCountrySet_expectUserCountryPage() {
        val authenticationToken = getAuthenticationToken("Test1", "ThisIsATest1234")

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/leaderboard/public?page=0&entriesPerPage=5&region=COUNTRY&filter=Austria")
                .header("Authorization", "Bearer $authenticationToken"),
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val jsonResponseString = result.response.contentAsString
        val response = objectMapper.readValue<RestResponsePage<LeaderBoardEntry>>(jsonResponseString)
        val retrievedBotNames = response.content.map { it.botName }

        assertTrue(
            retrievedBotNames.containsAll(continentViewAccount1Europe),
            "Not all expected bot names are present in the response"
        )
        assertTrue(
            retrievedBotNames == continentViewAccount1Europe,
            "Bot names order in the response doesn't match the expected order"
        )
    }

    @Test
    fun whenGetPrivateLeaderboardGlobalView_withJWT_expectGlobalViewPage() {
        val authenticationToken = getAuthenticationToken("Test1", "ThisIsATest1234")

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/leaderboard/private?page=0&entriesPerPage=5&region=GLOBAL")
                .header("Authorization", "Bearer $authenticationToken"),
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val jsonResponseString = result.response.contentAsString
        val response = objectMapper.readValue<RestResponsePage<LeaderBoardEntry>>(jsonResponseString)
        val retrievedBotNames = response.content.map { it.botName }

        assertTrue(
            retrievedBotNames.containsAll(continentViewAccount1Europe),
            "Not all expected bot names are present in the response"
        )
        assertTrue(
            retrievedBotNames == continentViewAccount1Europe,
            "Bot names order in the response doesn't match the expected order"
        )
    }

    @Test
    fun whenGetPrivateLeaderboardContinentView_withJWT_withUserContinentSet_expectUserContinentPage() {
        val authenticationToken = getAuthenticationToken("Test1", "ThisIsATest1234")

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/leaderboard/private?page=0&entriesPerPage=5&region=GLOBAL")
                .header("Authorization", "Bearer $authenticationToken"),
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val jsonResponseString = result.response.contentAsString
        val response = objectMapper.readValue<RestResponsePage<LeaderBoardEntry>>(jsonResponseString)
        val retrievedBotNames = response.content.map { it.botName }

        assertTrue(
            retrievedBotNames.containsAll(continentViewAccount1Europe),
            "Not all expected bot names are present in the response"
        )
        assertTrue(
            retrievedBotNames == continentViewAccount1Europe,
            "Bot names order in the response doesn't match the expected order"
        )
    }

    @Test
    fun whenGetPrivateLeaderboardCountryView_withJWT_withUserCountrySet_expectUserCountryPage() {
        val authenticationToken = getAuthenticationToken("Test1", "ThisIsATest1234")

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/leaderboard/private?page=0&entriesPerPage=5&region=GLOBAL")
                .header("Authorization", "Bearer $authenticationToken"),
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val jsonResponseString = result.response.contentAsString
        val response = objectMapper.readValue<RestResponsePage<LeaderBoardEntry>>(jsonResponseString)
        val retrievedBotNames = response.content.map { it.botName }

        assertTrue(
            retrievedBotNames.containsAll(continentViewAccount1Europe),
            "Not all expected bot names are present in the response"
        )
        assertTrue(
            retrievedBotNames == continentViewAccount1Europe,
            "Bot names order in the response doesn't match the expected order"
        )
    }

    private fun getAuthenticationToken(username: String, password: String): String {
        return TestHelper.getInitialAuthenticationTokens(objectMapper, mockMvc, username, password).accessToken.token
    }
}
