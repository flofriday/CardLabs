package at.tuwien.ase.cardlabs.management

import at.tuwien.ase.cardlabs.management.controller.model.Account
import at.tuwien.ase.cardlabs.management.service.AccountService
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails

class TestHelper {

    companion object {

        fun createUserDetails(username: String, password: String): UserDetails {
            return User(username, password, listOf(SimpleGrantedAuthority("ROLE_USER")))
        }

        fun createAccount(accountService: AccountService, username: String, email: String, password: String): Account {
            val account = Account(
                id = null,
                username = username,
                email = email,
                password = password
            )
            return accountService.create(account)
        }

        fun createAccountCreateJSON(username: String, email: String, password: String): String {
            return """
                {
                    "username": "$username",
                    "email": "$email",
                    "password": "$password"
                }
            """.trimIndent()
        }

        fun createAccountLoginJSON(username: String, password: String): String {
            return """
                {
                    "username": "$username",
                    "password": "$password"
                }
            """.trimIndent()
        }
    }
}
