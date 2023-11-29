package at.tuwien.ase.cardlabs.management.ut

import at.tuwien.ase.cardlabs.management.TestHelper
import at.tuwien.ase.cardlabs.management.controller.model.Account
import at.tuwien.ase.cardlabs.management.database.model.LocationDAO
import at.tuwien.ase.cardlabs.management.database.repository.AccountRepository
import at.tuwien.ase.cardlabs.management.database.repository.LocationRepository
import at.tuwien.ase.cardlabs.management.error.AccountExistsException
import at.tuwien.ase.cardlabs.management.service.AccountService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class AccountServiceTests {

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var locationRepository: LocationRepository

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
    fun whenAccountCreate_withIdSet_expectIllegalIdError() {
        val account = Account(
            id = 1L,
            username = "test",
            email = "test@test.com",
            password = "password",
            location = null,
            sendScoreUpdates = true,
            sendNewsletter = true,
            sendChangeUpdates = true,
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
            password = "password",
            location = "Austria",
            sendScoreUpdates = true,
            sendNewsletter = true,
            sendChangeUpdates = true,
        )

        val created = assertDoesNotThrow {
            accountService.create(account)
        }
        assertNotNull(created)
        assertNotNull(created.id)
        assertEquals("test", created.username)
        assertEquals("test@test.com", created.email)
        assertEquals("REDACTED", created.password)
        assertEquals("Austria", created.location)
        assertEquals(true, created.sendScoreUpdates)
        assertEquals(true, created.sendNewsletter)
        assertEquals(true, created.sendChangeUpdates)

        val found = accountRepository.findByUsernameAndDeletedIsNull(created.username)
        assertNotNull(found)
        assertEquals(created.id, found?.id)
    }

    @Test
    fun whenAccountCreate_withExistingUsername_expectAccountExistUsernameError() {
        createAccount("test", "test@test.com", "password", null, true, true, true)

        val account = Account(
            id = null,
            username = "test",
            email = "test@test.com",
            password = "password",
            location = "Austria",
            sendScoreUpdates = true,
            sendNewsletter = true,
            sendChangeUpdates = true,
        )

        val exception = assertThrows<AccountExistsException> {
            accountService.create(account)
        }
        assertTrue(exception.message?.startsWith("An account with the username") ?: false)
    }

    @Test
    fun whenAccountCreate_withExistingEmail_expectAccountExistEmailError() {
        createAccount("test", "test@test.com", "password", null, true, true, true)

        val account = Account(
            id = null,
            username = "test2",
            email = "test@test.com",
            password = "password",
            location = "Austria",
            sendScoreUpdates = true,
            sendNewsletter = true,
            sendChangeUpdates = true,
        )

        val exception = assertThrows<AccountExistsException> {
            accountService.create(account)
        }
        assertTrue(exception.message?.startsWith("An account with the email") ?: false)
    }

    @Test
    fun whenAccountDelete_expectSuccess() {
        val account = createAccount("test", "test@test.com", "password", null, true, true, true)
        val userDetailsAccount = TestHelper.createUserDetails(account.id!!, account.username, account.email, account.password)

        assertDoesNotThrow {
            accountService.delete(userDetailsAccount)
        }
    }

    private fun createAccount(username: String, email: String, password: String, location: String?, sendScoreUpdates: Boolean, sendChangeUpdates: Boolean, sendNewsletter: Boolean): Account {
        return TestHelper.createAccount(accountService, username, email, password, location, sendScoreUpdates, sendChangeUpdates, sendNewsletter)
    }
}
