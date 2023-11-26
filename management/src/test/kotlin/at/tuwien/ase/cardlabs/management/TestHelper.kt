package at.tuwien.ase.cardlabs.management

import at.tuwien.ase.cardlabs.management.controller.model.Account
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.service.AccountService

class TestHelper {

    companion object {

        fun createUserDetails(id: Long, username: String, email: String, password: String): CardLabUser {
            return CardLabUser(id, username, email, password)
        }

        fun createAccount(accountService: AccountService, username: String, email: String, password: String): Account {
            val account = Account(
                id = null,
                username = username,
                email = email,
                password = password,
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
