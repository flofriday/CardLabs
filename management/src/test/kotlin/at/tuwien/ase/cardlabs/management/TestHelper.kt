package at.tuwien.ase.cardlabs.management

import at.tuwien.ase.cardlabs.management.controller.model.account.Account
import at.tuwien.ase.cardlabs.management.controller.model.bot.Bot
import at.tuwien.ase.cardlabs.management.controller.model.bot.BotCreate
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.service.AccountService
import at.tuwien.ase.cardlabs.management.service.bot.BotService

class TestHelper {

    companion object {

        // == Authentication ==
        fun createUserDetails(id: Long, username: String, email: String, password: String): CardLabUser {
            return CardLabUser(id, username, email, password)
        }

        fun createUserDetails(account: Account): CardLabUser {
            return CardLabUser(account.id!!, account.username, account.email, account.password)
        }

        // == Account ==
        fun createAccount(
            accountService: AccountService,
            username: String,
            email: String,
            password: String,
            location: String?,
            sendScoreUpdates: Boolean,
            sendChangeUpdates: Boolean,
            sendNewsletter: Boolean
        ): Account {
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

        fun getAccount(accountService: AccountService, username: String): Account {
            return accountService.getUser(username)
        }

        fun createAccountCreateJSON(
            username: String,
            email: String,
            password: String,
            location: String?,
            sendScoreUpdates: Boolean,
            sendChangeUpdates: Boolean,
            sendNewsletter: Boolean
        ): String {
            return """
                {
                    "username": "$username",
                    "email": "$email",
                    "password": "$password",
                    "location": ${if (location == null) null else "\"" + location + "\""},
                    "sendScoreUpdates": "$sendScoreUpdates",
                    "sendChangeUpdates": "$sendChangeUpdates",
                    "sendNewsletter": "$sendNewsletter"
                }
            """.trimIndent()
        }

        fun createAccountUpdateCreateJSON(
            location: String?,
            sendScoreUpdates: Boolean,
            sendChangeUpdates: Boolean,
            sendNewsletter: Boolean
        ): String {
            return """
                {
                    "location": ${if (location == null) null else "\"" + location + "\""},
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

        // == Bot ==
        fun createBot(botService: BotService, user: CardLabUser, name: String, code: String?): Bot {
            val botCreate = BotCreate(
                name = name,
                currentCode = code
            )
            return botService.create(user, botCreate)
        }
    }
}
