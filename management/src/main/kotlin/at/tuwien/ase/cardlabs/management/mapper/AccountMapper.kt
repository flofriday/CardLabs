package at.tuwien.ase.cardlabs.management.mapper

import at.tuwien.ase.cardlabs.management.controller.model.account.Account
import at.tuwien.ase.cardlabs.management.database.model.AccountDAO
import org.springframework.stereotype.Component

@Component
class AccountMapper {

    fun map(account: Account): AccountDAO {
        val accountDAO = AccountDAO()
        accountDAO.id = account.id
        accountDAO.username = account.username
        accountDAO.email = account.email
        return accountDAO
    }

    fun map(accountDAO: AccountDAO): Account {
        // deliberately not map the password to not leak it, even if it is hashed
        // this can be done as we are passing data away from the center of the application
        // account.password = accountDAO.password
        return Account(
            id = accountDAO.id,
            username = accountDAO.username,
            email = accountDAO.email,
            location = accountDAO.location?.name,
        )
    }
}
