package at.tuwien.ase.cardlabs.management.security.config

import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import java.io.IOException


class Oauth2AuthenticationEntrypoint : AuthenticationEntryPoint {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Throws(IOException::class, ServletException::class)
    override fun commence(
        request: HttpServletRequest?, response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        logger.debug("Oauth2AuthenticationEntrypoint.commence, authException: {}", authException.toString())
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = "application/json"
        response.writer.println(("{ \"error\": \"" + authException.message).toString() + "\" }")
        response.writer.flush()
    }
}