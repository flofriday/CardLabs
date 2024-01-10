package at.tuwien.ase.cardlabs.management

import at.tuwien.ase.cardlabs.management.controller.model.account.Account
import at.tuwien.ase.cardlabs.management.controller.model.bot.Bot
import at.tuwien.ase.cardlabs.management.controller.model.bot.BotCreate
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.security.authentication.AuthenticationResponse
import at.tuwien.ase.cardlabs.management.service.AccountService
import at.tuwien.ase.cardlabs.management.service.bot.BotService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class TestHelper {

    companion object {

        val DEFAULT_PASSWORD: String = "PassWord123?!"

        // == Authentication ==
        @JvmStatic
        fun createUserDetails(id: Long, username: String, email: String, password: String): CardLabUser {
            return CardLabUser(id, username, email, password)
        }

        @JvmStatic
        fun getAuthenticationToken(mockMvc: MockMvc, username: String, password: String): String {
            val body = createAccountLoginJSON(username, password)
            val result = mockMvc.perform(
                MockMvcRequestBuilders.post("/authentication/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body),
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
            val response = jacksonObjectMapper().readValue<AuthenticationResponse>(result.response.contentAsString)
            return response.jwt
        }

        @JvmStatic
        fun createUserDetails(account: Account): CardLabUser {
            return CardLabUser(account.id!!, account.username, account.email, account.password)
        }

        // == Account ==
        @JvmStatic
        fun createAccount(
            accountService: AccountService,
            username: String,
            email: String,
            password: String,
            location: String?,
            sendScoreUpdates: Boolean,
            sendChangeUpdates: Boolean,
            sendNewsletter: Boolean,
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

        @JvmStatic
        fun createAccount(accountService: AccountService): Account {
            return createAccount(
                accountService = accountService,
                username = "test",
                email = "test@test.com",
                password = DEFAULT_PASSWORD,
                location = null,
                sendScoreUpdates = false,
                sendChangeUpdates = false,
                sendNewsletter = false,
            )
        }

        @JvmStatic
        fun getAccount(accountService: AccountService, username: String): Account {
            return accountService.getUser(username)
        }

        @JvmStatic
        fun createAccountCreateJSON(
            username: String,
            email: String,
            password: String,
            location: String?,
            sendScoreUpdates: Boolean,
            sendChangeUpdates: Boolean,
            sendNewsletter: Boolean,
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

        @JvmStatic
        fun createAccountUpdateCreateJSON(
            location: String?,
            sendScoreUpdates: Boolean,
            sendChangeUpdates: Boolean,
            sendNewsletter: Boolean,
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

        @JvmStatic
        fun createAccountLoginJSON(username: String, password: String): String {
            return """
                {
                    "username": "$username",
                    "password": "$password"
                }
            """.trimIndent()
        }

        // == Bot ==
        @JvmStatic
        fun createBot(botService: BotService, user: CardLabUser, name: String, code: String?): Bot {
            val botCreate = BotCreate(
                name = name,
                currentCode = code,
            )
            return botService.create(user, botCreate)
        }
    }
}
