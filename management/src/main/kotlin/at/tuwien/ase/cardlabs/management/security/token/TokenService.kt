package at.tuwien.ase.cardlabs.management.security.token

import at.tuwien.ase.cardlabs.management.error.authentication.InvalidTokenException
import at.tuwien.ase.cardlabs.management.error.authentication.TokenExpiredException
import org.springframework.security.core.Authentication

/**
 * An interface that defines useful function for operations on tokens
 */
interface TokenService {

    fun generateRefreshToken(authentication: Authentication): TokenPair

    @Throws(TokenExpiredException::class, InvalidTokenException::class)
    fun generateAccessToken(refreshToken: String): Token

    @Throws(TokenExpiredException::class, InvalidTokenException::class)
    fun isValidRefreshToken(token: String): Boolean

    @Throws(TokenExpiredException::class, InvalidTokenException::class)
    fun isValidAccessToken(token: String): Boolean

    fun extractAccountUsername(token: String): String
}
