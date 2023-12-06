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
import org.springframework.context.annotation.Lazy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val locationRepository: LocationRepository,
    private val accountMapper: AccountMapper,
    @Lazy private val passwordEncoder: PasswordEncoder,
) {

    @Transactional
    fun create(account: Account): Account {
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

    @Transactional
    fun update(user: CardLabUser, accountUpdate: AccountUpdate) {
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

        accountRepository.save(account)
    }

    @Transactional
    fun delete(user: CardLabUser, id: Long) {
        Helper.requireNonNull(user, "No authentication provided")
        Helper.requireNonNull(id, "Cannot delete an account with the id null")
        if (user.id != id) {
            throw UnauthorizedException("Can't delete an account other than yourself")
        }

        val accountDao = findById(id)
        if (accountDao != null) {
            accountDao.deleted = Instant.now()
        }
    }

    fun getUser(username: String): Account {
        val account = findByUsername(username) ?: throw AccountNotFoundException("Account could not be found")
        return accountMapper.map(account)
    }

    fun findById(id: Long): AccountDAO? {
        return accountRepository.findByIdAndDeletedIsNull(id)
    }

    fun findByUsername(username: String?): AccountDAO? {
        if (username == null) {
            return null
        }
        return accountRepository.findByUsernameAndDeletedIsNull(username)
    }

    fun findLocation(name: String): LocationDAO? {
        return locationRepository.findByName(name)
    }

    fun findByEmail(email: String?): AccountDAO? {
        if (email == null) {
            return null
        }
        return accountRepository.findByEmailAndDeletedIsNull(email)
    }
}
