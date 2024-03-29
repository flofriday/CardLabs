package at.tuwien.ase.cardlabs.management.security

import at.tuwien.ase.cardlabs.management.database.model.AccountDAO
import at.tuwien.ase.cardlabs.management.service.AccountService
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class DatabaseUserDetailsService(private val accountService: AccountService) : UserDetailsService {

    override fun loadUserByUsername(username: String?): CardLabUser {
        val accountDAO: AccountDAO = accountService.findByEmail(username)
            ?: throw UsernameNotFoundException("No user with the email $username exists")
        return CardLabUser(accountDAO.id!!, accountDAO.username, accountDAO.email)
    }
}
