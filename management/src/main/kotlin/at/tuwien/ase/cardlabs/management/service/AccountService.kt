package at.tuwien.ase.cardlabs.management.service

import at.tuwien.ase.cardlabs.management.Helper
import at.tuwien.ase.cardlabs.management.controller.model.Account
import at.tuwien.ase.cardlabs.management.database.model.AccountDAO
import at.tuwien.ase.cardlabs.management.database.repository.AccountRepository
import at.tuwien.ase.cardlabs.management.error.AccountExistsException
import at.tuwien.ase.cardlabs.management.error.UnauthorizedException
import at.tuwien.ase.cardlabs.management.mapper.AccountMapper
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import org.springframework.context.annotation.Lazy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val accountMapper: AccountMapper,
    @Lazy private val passwordEncoder: PasswordEncoder,
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

    fun findById(id: Long): AccountDAO? {
        return accountRepository.findByIdAndDeletedIsNull(id)
    }

    fun findByUsername(username: String?): AccountDAO? {
        if (username == null) {
            return null
        }
        return accountRepository.findByUsernameAndDeletedIsNull(username)
    }

    fun findByEmail(email: String?): AccountDAO? {
        if (email == null) {
            return null
        }
        return accountRepository.findByEmailAndDeletedIsNull(email)
    }
}
