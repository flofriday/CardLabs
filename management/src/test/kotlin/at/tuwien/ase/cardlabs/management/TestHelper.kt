package at.tuwien.ase.cardlabs.management

import at.tuwien.ase.cardlabs.management.controller.model.account.Account
import at.tuwien.ase.cardlabs.management.controller.model.bot.Bot
import at.tuwien.ase.cardlabs.management.controller.model.bot.BotCreate
import at.tuwien.ase.cardlabs.management.database.repository.AccountRepository
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.security.authentication.AccessTokenAuthenticationResponse
import at.tuwien.ase.cardlabs.management.security.authentication.AuthenticationResponse
import at.tuwien.ase.cardlabs.management.security.jwt.JwtTokenService
import at.tuwien.ase.cardlabs.management.service.AccountService
import at.tuwien.ase.cardlabs.management.service.bot.BotService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class TestHelper {

    companion object {
        // == Authentication ==
        @JvmStatic
        fun createUserDetails(id: Long, username: String, email: String): CardLabUser {
            return CardLabUser(id, username, email)
        }

        @JvmStatic
        fun getInitialAuthenticationTokens(
            jwtTokenService: JwtTokenService,
            accountRepository: AccountRepository,
            username: String,
        ): AuthenticationResponse {
            val userAccount = accountRepository.findByUsernameAndDeletedIsNull(username)
            if (userAccount?.id == null) {
                throw Exception()
            }
            val cardLabUser = CardLabUser(userAccount.id!!, userAccount.username, userAccount.email)
            val tokenPair = jwtTokenService.generateTokenPair(cardLabUser)
            return AuthenticationResponse(tokenPair.refreshToken, tokenPair.accessToken)
        }

        @JvmStatic
        fun getAccessToken(
            objectMapper: ObjectMapper,
            mockMvc: MockMvc,
            refreshToken: String
        ): AccessTokenAuthenticationResponse {
            val body = createAuthenticationRefreshJSON(refreshToken)
            val result = mockMvc.perform(
                MockMvcRequestBuilders.post("/authentication/refresh")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body),
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
            return objectMapper.readValue<AccessTokenAuthenticationResponse>(result.response.contentAsString)
        }

        @JvmStatic
        fun createUserDetails(account: Account): CardLabUser {
            return CardLabUser(account.id!!, account.username, account.email)
        }

        // == Account ==
        @JvmStatic
        fun createAccount(
            accountService: AccountService,
            username: String,
            email: String,
            location: String?,
        ): Account {
            val account = Account(
                id = null,
                username = username,
                email = email,
                location = location,
            )
            return accountService.create(account)
        }

        @JvmStatic
        fun createAccount(accountService: AccountService): Account {
            return createAccount(
                accountService = accountService,
                username = "test",
                email = "test@test.com",
                location = null,
            )
        }

        @JvmStatic
        fun getAccount(accountService: AccountService, username: String): Account {
            return accountService.fetchUser(username)
        }

        @JvmStatic
        fun createAccountCreateJSON(
            username: String,
            email: String,
            location: String?,
        ): String {
            return """
                {
                    "username": "$username",
                    "email": "$email",
                    "location": ${if (location == null) null else "\"" + location + "\""}
                }
            """.trimIndent()
        }

        @JvmStatic
        fun createAccountUpdateCreateJSON(
            location: String?,
        ): String {
            return """
                {
                    "location": ${if (location == null) null else "\"" + location + "\""}
                }
            """.trimIndent()
        }

        @JvmStatic
        fun createAccountLoginJSON(username: String): String {
            return """
                {
                    "username": "$username"
                }
            """.trimIndent()
        }

        @JvmStatic
        fun createAuthenticationRefreshJSON(refreshToken: String): String {
            return """
                {
                    "refreshToken": "$refreshToken"
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
