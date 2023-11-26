package at.tuwien.ase.cardlabs.management.service

import at.tuwien.ase.cardlabs.management.Helper
import at.tuwien.ase.cardlabs.management.controller.model.Account
import at.tuwien.ase.cardlabs.management.controller.model.AccountUpdate
import at.tuwien.ase.cardlabs.management.database.model.AccountDAO
import at.tuwien.ase.cardlabs.management.database.model.LocationDAO
import at.tuwien.ase.cardlabs.management.database.repository.AccountRepository
import at.tuwien.ase.cardlabs.management.database.repository.LocationRepository
import at.tuwien.ase.cardlabs.management.error.AccountExistsException
import at.tuwien.ase.cardlabs.management.error.AccountNotFoundException
import at.tuwien.ase.cardlabs.management.error.LocationNotFoundException
import at.tuwien.ase.cardlabs.management.mapper.AccountMapper
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import org.springframework.context.annotation.Lazy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

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
        Helper.requireNonNull(account.username, "The username must be set")
        Helper.requireNonNull(account.email, "The email must be set")
        Helper.requireNonNull(account.password, "The password must be set")
        Helper.requireNonNull(account.sendChangeUpdates, "The SendChangeUpdates option must be set")
        Helper.requireNonNull(account.sendScoreUpdates, "The SendScoreUpdates option must be set")
        Helper.requireNonNull(account.sendNewsletter, "The SendNewsletter option must be set")
        val location: LocationDAO? = if (account.location != null) findLocation(account.location) else null
        if (account.location != null && location == null) {
            throw LocationNotFoundException("Location with name ${account.location} does not exist")
        }
        if (findByUsername(account.username) != null) {
            throw AccountExistsException("An account with the username ${account.username} already exists")
        }
        if (findByEmail(account.email) != null) {
            throw AccountExistsException("An account with the email ${account.email} already exists")
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

    fun delete(user: CardLabUser) {
        accountRepository.deleteById(user.id)
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

    fun findById(id: Long): Optional<AccountDAO?> {
        return accountRepository.findById(id)
    }

    fun getUser(username: String): Account {
        val account = findByUsername(username) ?: throw AccountNotFoundException("Account could not be found")
        return accountMapper.map(account)
    }

    fun findByUsername(username: String?): AccountDAO? {
        if (username == null) {
            return null
        }
        return accountRepository.findByUsername(username)
    }

    fun findLocation(name: String): LocationDAO? {
        return locationRepository.findByName(name)
    }

    fun findByEmail(email: String?): AccountDAO? {
        if (email == null) {
            return null
        }
        return accountRepository.findByEmail(email)
    }
}
