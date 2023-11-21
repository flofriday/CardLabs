package at.tuwien.ase.cardlabs.management.ut

import at.tuwien.ase.cardlabs.management.TestHelper
import at.tuwien.ase.cardlabs.management.controller.model.Account
import at.tuwien.ase.cardlabs.management.database.repository.AccountRepository
import at.tuwien.ase.cardlabs.management.error.AccountExistsException
import at.tuwien.ase.cardlabs.management.error.UnauthorizedException
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

    @BeforeEach
    fun beforeEach() {
        accountRepository.deleteAll()
    }

    @Test
    fun whenAccountCreate_expectIllegalIdError() {
        val account = Account(
            id = 1L,
            username = "test",
            email = "test@test.com",
            password = "password"
        )

        val exception = assertThrows<IllegalArgumentException> {
            accountService.create(account)
        }
        assertEquals("The id must not be set", exception.message)
    }

    @Test
    fun whenAccountCreate_expectSuccess() {
        val account = Account(
            id = null,
            username = "test",
            email = "test@test.com",
            password = "password"
        )

        val created = assertDoesNotThrow {
            accountService.create(account)
        }
        assertNotNull(created)
        assertNotNull(created.id)
        assertEquals("test", created.username)
        assertEquals("test@test.com", created.email)
        assertEquals("REDACTED", created.password)

        val found = accountRepository.findByUsername(created.username)
        assertNotNull(found)
        assertEquals(created.id, found?.id)
    }

    @Test
    fun whenAccountCreate_expectAccountExistUsernameError() {
        createAccount("test", "test@test.com", "password")

        val account = Account(
            id = null,
            username = "test",
            email = "test@test.com",
            password = "password"
        )

        val exception = assertThrows<AccountExistsException> {
            accountService.create(account)
        }
        assertTrue(exception.message?.startsWith("An account with the username") ?: false)
    }

    @Test
    fun whenAccountCreate_expectAccountExistEmailError() {
        createAccount("test", "test@test.com", "password")

        val account = Account(
            id = null,
            username = "test2",
            email = "test@test.com",
            password = "password"
        )

        val exception = assertThrows<AccountExistsException> {
            accountService.create(account)
        }
        assertTrue(exception.message?.startsWith("An account with the email") ?: false)
    }

    @Test
    fun whenAccountDelete_expectSuccess() {
        val account = createAccount("test", "test@test.com", "password")
        val userDetailsAccount = TestHelper.createUserDetails(account.username, account.password)

        assertDoesNotThrow {
            accountService.delete(userDetailsAccount, account.id!!)
        }
    }

    @Test
    fun whenAccountDelete_expectUnauthorizedError() {
        val account1 = createAccount("test", "test@test.com", "password")
        val account2 = createAccount("test2", "test2@test.com", "password")
        val userDetailsAccount1 = TestHelper.createUserDetails(account1.username, account1.password)

        val exception = assertThrows<UnauthorizedException> {
            accountService.delete(userDetailsAccount1, account2.id!!)
        }
        assertEquals("Can't delete an account other than yourself", exception.message)
    }

    private fun createAccount(username: String, email: String, password: String): Account {
        return TestHelper.createAccount(accountService, username, email, password)
    }
}
