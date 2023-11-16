package at.tuwien.ase.cardlabs.management.mapper

import at.tuwien.ase.cardlabs.management.controller.model.Account
import at.tuwien.ase.cardlabs.management.database.model.AccountDAO
import org.springframework.stereotype.Component

// TODO: replace by a library that automatically does the mapping
@Component
class AccountMapper {

    fun map(account: Account): AccountDAO {
        val accountDAO = AccountDAO()
        accountDAO.id = account.id
        accountDAO.username = account.username
        accountDAO.password = account.password.toString()
        return accountDAO
    }

    fun map(accountDAO: AccountDAO): Account {
        val account = Account()
        account.id = accountDAO.id
        account.username = accountDAO.username
        // deliberately not map the password to not leak it, even if it is hashed
        // this can be done as we are passing data away from the center of the application
        // account.password = accountDAO.password
        return account
    }

}