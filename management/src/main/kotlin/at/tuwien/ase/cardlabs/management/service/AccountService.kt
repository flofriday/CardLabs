package at.tuwien.ase.cardlabs.management.service

import at.tuwien.ase.cardlabs.management.Helper
import at.tuwien.ase.cardlabs.management.controller.model.account.Account
import at.tuwien.ase.cardlabs.management.controller.model.account.AccountUpdate
import at.tuwien.ase.cardlabs.management.database.model.AccountDAO
import at.tuwien.ase.cardlabs.management.database.model.LocationDAO
import at.tuwien.ase.cardlabs.management.database.repository.AccountRepository
import at.tuwien.ase.cardlabs.management.database.repository.LocationRepository
import at.tuwien.ase.cardlabs.management.error.UnauthorizedException
import at.tuwien.ase.cardlabs.management.error.account.AccountExistsException
import at.tuwien.ase.cardlabs.management.error.account.AccountNotFoundException
import at.tuwien.ase.cardlabs.management.error.account.LocationNotFoundException
import at.tuwien.ase.cardlabs.management.mapper.AccountMapper
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.validation.validator.AccountValidator
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Lazy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import kotlin.jvm.Throws

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val locationRepository: LocationRepository,
    private val accountMapper: AccountMapper,
    @Lazy private val passwordEncoder: PasswordEncoder,
) {

    private final val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Create an account
     */
    @Transactional
    @Throws(
        AccountExistsException::class,
        LocationNotFoundException::class
    )
    fun create(account: Account): Account {
        logger.debug("Attempting to create an account with the username ${account.username}")
        Helper.requireNull(account.id, "The id must not be set")

        AccountValidator.validateAccountCreate(account)

        if (findByUsername(account.username) != null) {
            throw AccountExistsException("An account with the username ${account.username} already exists")
        }
        if (findByEmail(account.email) != null) {
            throw AccountExistsException("An account with the email ${account.email} already exists")
        }

        val location: LocationDAO? = account.location?.let { findLocation(account.location) }
        if (account.location != null && location == null) {
            throw LocationNotFoundException("Location with name ${account.location} does not exist")
        }

        val acc = AccountDAO()
        acc.username = account.username
        acc.email = account.email
        acc.password = passwordEncoder.encode(account.password)
        acc.location = location
        acc.sendChangeUpdates = account.sendChangeUpdates
        acc.sendScoreUpdates = account.sendScoreUpdates
        acc.sendNewsletter = account.sendNewsletter
        return accountMapper.map(accountRepository.save(acc))
    }

    /**
     * Update an account
     */
    @Transactional
    @Throws(
        AccountNotFoundException::class,
        LocationNotFoundException::class
    )
    fun update(user: CardLabUser, accountUpdate: AccountUpdate): Account {
        logger.debug("User ${user.id} attempts to update its account")
        Helper.requireNonNull(user, "No authentication provided")
        val account = findByUsername(user.username) ?: throw AccountNotFoundException("Account could not be found")

        val location: LocationDAO? = if (accountUpdate.location != null) findLocation(accountUpdate.location) else null
        if (accountUpdate.location != null && location == null) {
            throw LocationNotFoundException("Location with name ${accountUpdate.location} does not exist")
        }
        account.location = location
        account.sendNewsletter = accountUpdate.sendNewsletter
        account.sendScoreUpdates = accountUpdate.sendScoreUpdates
        account.sendChangeUpdates = accountUpdate.sendChangeUpdates

        return accountMapper.map(accountRepository.save(account))
    }

    /**
     * Delete an account
     */
    @Transactional
    @Throws(UnauthorizedException::class)
    fun delete(user: CardLabUser, accountId: Long) {
        logger.debug("User ${user.id} attempts to delete the account $accountId")
        Helper.requireNonNull(user, "No authentication provided")
        Helper.requireNonNull(accountId, "Cannot delete an account with the id null")
        if (user.id != accountId) {
            throw UnauthorizedException("Can't delete an account other than yourself")
        }

        val accountDao = findById(accountId)
        if (accountDao != null) {
            accountDao.deleted = Instant.now()
        }
    }

    /**
     * Fetch an account from a CardLabUser
     */
    @Transactional
    fun fetchUser(user: CardLabUser): Account {
        logger.debug("User ${user.id} attempts to fetch its account information")
        return fetchUser(user.username)
    }

    /**
     * Fetch an account by its username
     */
    @Transactional
    @Throws(AccountNotFoundException::class)
    fun fetchUser(username: String): Account {
        val account = findByUsername(username) ?: throw AccountNotFoundException("Account could not be found")
        return accountMapper.map(account)
    }

    /**
     * Fetch an account by its id
     */
    @Transactional
    fun findById(id: Long): AccountDAO? {
        return accountRepository.findByIdAndDeletedIsNull(id)
    }

    /**
     * Fetch an account by its username
     */
    @Transactional
    fun findByUsername(username: String?): AccountDAO? {
        if (username == null) {
            return null
        }
        return accountRepository.findByUsernameAndDeletedIsNull(username)
    }

    private fun findLocation(name: String): LocationDAO? {
        return locationRepository.findByName(name)
    }

    private fun findByEmail(email: String?): AccountDAO? {
        if (email == null) {
            return null
        }
        return accountRepository.findByEmailAndDeletedIsNull(email)
    }
}
