package at.tuwien.ase.cardlabs.management.ut

import at.tuwien.ase.cardlabs.management.ApplicationTest
import at.tuwien.ase.cardlabs.management.TestHelper
import at.tuwien.ase.cardlabs.management.controller.model.account.Account
import at.tuwien.ase.cardlabs.management.database.model.LocationDAO
import at.tuwien.ase.cardlabs.management.database.repository.AccountRepository
import at.tuwien.ase.cardlabs.management.database.repository.LocationRepository
import at.tuwien.ase.cardlabs.management.error.account.AccountExistsException
import at.tuwien.ase.cardlabs.management.service.AccountService
import at.tuwien.ase.cardlabs.management.util.Continent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext

@ApplicationTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
internal class AccountServiceTests {

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var locationRepository: LocationRepository

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
    fun whenAccountCreate_withIdSet_expectIllegalIdError() {
        val account = Account(
            id = 1L,
            username = "test",
            email = "test@test.com",
            location = null,
        )

        val exception = assertThrows<IllegalArgumentException> {
            accountService.create(account)
        }
        assertEquals("The id must not be set", exception.message)
    }

    @Test
    fun whenAccountCreate_withValidAccountData_expectSuccess() {
        val account = Account(
            id = null,
            username = "test",
            email = "test@test.com",
            location = "Austria",
        )

        val created = assertDoesNotThrow {
            accountService.create(account)
        }
        assertNotNull(created)
        assertNotNull(created.id)
        assertEquals("test", created.username)
        assertEquals("test@test.com", created.email)
        assertEquals("Austria", created.location)

        val found = accountRepository.findByUsernameAndDeletedIsNull(created.username)
        assertNotNull(found)
        assertEquals(created.id, found?.id)
    }

    @Test
    fun whenAccountCreate_withExistingUsername_expectAccountExistUsernameError() {
        createAccount("test", "test@test.com", null)

        val account = Account(
            id = null,
            username = "test",
            email = "test@test.com",
            location = "Austria",
        )

        val exception = assertThrows<AccountExistsException> {
            accountService.create(account)
        }
        assertTrue(exception.message?.startsWith("An account with the username") ?: false)
    }

    @Test
    fun whenAccountCreate_withExistingEmail_expectAccountExistEmailError() {
        createAccount("test", "test@test.com", null)

        val account = Account(
            id = null,
            username = "test2",
            email = "test@test.com",
            location = "Austria",
        )

        val exception = assertThrows<AccountExistsException> {
            accountService.create(account)
        }
        assertTrue(exception.message?.startsWith("An account with the email") ?: false)
    }

    @Test
    fun whenAccountDelete_expectSuccess() {
        val account = createAccount("test", "test@test.com", null)
        val userDetailsAccount =
            TestHelper.createUserDetails(account.id!!, account.username, account.email)

        assertDoesNotThrow {
            accountService.delete(userDetailsAccount, userDetailsAccount.id)
        }
    }

    private fun createAccount(
        username: String,
        email: String,
        location: String?,
    ): Account {
        return TestHelper.createAccount(
            accountService,
            username,
            email,
            location
        )
    }
}
