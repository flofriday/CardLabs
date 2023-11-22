package at.tuwien.ase.cardlabs.management.service

import at.tuwien.ase.cardlabs.management.Helper
import at.tuwien.ase.cardlabs.management.controller.model.Account
import at.tuwien.ase.cardlabs.management.database.model.AccountDAO
import at.tuwien.ase.cardlabs.management.database.repository.AccountRepository
import at.tuwien.ase.cardlabs.management.error.AccountExistsException
import at.tuwien.ase.cardlabs.management.error.AccountNotFoundException
import at.tuwien.ase.cardlabs.management.error.UnauthorizedException
import at.tuwien.ase.cardlabs.management.mapper.AccountMapper
import org.springframework.context.annotation.Lazy
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val accountMapper: AccountMapper,
    @Lazy private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun create(account: Account): Account {
        Helper.requireNull(account.id, "The id must not be set")
        Helper.requireNonNull(account.username, "The username must be set")
        Helper.requireNonNull(account.email, "The email must be set")
        Helper.requireNonNull(account.password, "The password must be set")
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
        return accountMapper.map(accountRepository.save(acc))
    }

    fun delete(user: UserDetails, id: Long) {
        Helper.requireNonNull(user, "No authentication provided")
        Helper.requireNonNull(id, "Cannot delete an account with the id null")
        val account = findByUsername(user.username)
            ?: throw IllegalArgumentException("No account with the username ${user.username}")
        if (account.id != id) {
            throw UnauthorizedException("Can't delete an account other than yourself")
        }
        accountRepository.deleteById(id)
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

    fun findByEmail(email: String?): AccountDAO? {
        if (email == null) {
            return null
        }
        return accountRepository.findByEmail(email)
    }
}
