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

        fun createAccount(accountService: AccountService, username: String, email: String, password: String, location: String?, sendScoreUpdates: Boolean, sendChangeUpdates: Boolean, sendNewsletter: Boolean): Account {
            val account = Account(
                id = null,
                username = username,
                email = email,
                password = password,
                location = location,
                sendScoreUpdates = sendScoreUpdates,
                sendChangeUpdates = sendChangeUpdates,
                sendNewsletter = sendNewsletter,
            )
            return accountService.create(account)
        }

        fun createAccountCreateJSON(username: String, email: String, password: String, location: String?, sendScoreUpdates: Boolean, sendChangeUpdates: Boolean, sendNewsletter: Boolean): String {
            return """
                {
                    "username": "$username",
                    "email": "$email",
                    "password": "$password",
                    "location": "$location",
                    "sendScoreUpdates": "$sendScoreUpdates",
                    "sendChangeUpdates": "$sendChangeUpdates",
                    "sendNewsletter": "$sendNewsletter"
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
