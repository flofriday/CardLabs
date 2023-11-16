package at.tuwien.ase.cardlabs.management.service

import at.tuwien.ase.cardlabs.management.Helper
import at.tuwien.ase.cardlabs.management.controller.model.Account
import at.tuwien.ase.cardlabs.management.database.model.AccountDAO
import at.tuwien.ase.cardlabs.management.database.repository.AccountRepository
import at.tuwien.ase.cardlabs.management.error.AccountExistsException
import at.tuwien.ase.cardlabs.management.mapper.AccountMapper
import org.springframework.context.annotation.Lazy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val accountMapper: AccountMapper,
    @Lazy private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun create(account: Account): Account {
        Helper.requireNull(account.id, "Can't create the account ${account.username} as it already contains an id")
        Helper.requireNonNull(account.password, "The password must be set")
        if (findByUsername(account.username) != null) {
            throw AccountExistsException("An account with the username ${account.username} already exists")
        }

        val acc = AccountDAO()
        acc.username = account.username
        acc.password = passwordEncoder.encode(account.password)
        return accountMapper.map(accountRepository.save(acc))
    }

    fun findById(id: Long): Optional<AccountDAO?> {
        return accountRepository.findById(id)
    }

    fun findByUsername(username: String?): AccountDAO? {
        if (username == null) {
            return null;
        }
        return accountRepository.findByUsername(username)
    }

}