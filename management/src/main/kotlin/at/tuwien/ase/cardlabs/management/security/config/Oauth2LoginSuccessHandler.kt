package at.tuwien.ase.cardlabs.management.security.config

import at.tuwien.ase.cardlabs.management.controller.model.account.Account
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.security.jwt.JwtTokenService
import at.tuwien.ase.cardlabs.management.service.AccountService
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import java.io.IOException
import java.util.Locale

class Oauth2LoginSuccessHandler(val accountService: AccountService, val jwtTokenService: JwtTokenService) :
    AuthenticationSuccessHandler {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {
        logger.info("Oauth2 authentication success: $authentication")
        logger.info("${authentication.principal}")
        logger.info(authentication.details.toString())
        logger.info(authentication.authorities.toString())
        logger.info(authentication.credentials.toString())

        val attributes = (authentication.principal as DefaultOAuth2User).attributes

        var username = (attributes["login"] ?: attributes["name"]) as String
        username = username.replace(" ", "_")
        var email = (attributes["email"] ?: "$username@cardlabs.local".lowercase(Locale.getDefault())) as String

        logger.info("Oauth2 authentication success: $email")

        // Find the user or create it
        var account = accountService.findByEmail(email)
        if (account == null) {
            logger.info("Creating new user")
            accountService.create(Account(null, username, email, null))
            account = accountService.findByEmail(email)!!
        }

        val tokenPair = jwtTokenService.generateTokenPair(CardLabUser(account.id!!, account.username, account.email))
        logger.info(tokenPair.refreshToken.token)
        response.sendRedirect("http://localhost:3000/login/success?refresh_token=${tokenPair.refreshToken.token}")
    }
}
