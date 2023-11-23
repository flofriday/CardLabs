package at.tuwien.ase.cardlabs.management

import at.tuwien.ase.cardlabs.management.controller.model.Account
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.service.AccountService

class TestHelper {

    companion object {

        fun createUserDetails(id: Long, username: String, email: String, password: String): CardLabUser {
            return CardLabUser(id, username, email, password)
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
                    "location": ${if (location == null) null else "$location"},
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
