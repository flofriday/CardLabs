package at.tuwien.ase.cardlabs.management.security.token

import at.tuwien.ase.cardlabs.management.error.authentication.InvalidTokenException
import at.tuwien.ase.cardlabs.management.error.authentication.TokenExpiredException
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import org.springframework.security.core.Authentication

/**
 * An interface that defines useful function for operations on tokens
 */
interface TokenService {

    /**
     * Generate a token pair containing a refresh token and access token
     */
    fun generateTokenPair(authentication: Authentication): TokenPair

    /**
     * Generate a token pair containing a refresh token and access token
     */
    fun generateTokenPair(cardLabUser: CardLabUser): TokenPair

    /**
     * Generate an access token from a refresh token
     */
    @Throws(TokenExpiredException::class, InvalidTokenException::class)
    fun generateAccessToken(refreshToken: String): Token

    /**
     * Verify if a refresh token is valid. If not throw an exception.
     */
    @Throws(TokenExpiredException::class, InvalidTokenException::class)
    fun verifyValidRefreshToken(token: String): Unit

    /**
     * Verify if an access token is valid. If not throw an exception.
     */
    @Throws(TokenExpiredException::class, InvalidTokenException::class)
    fun verifyValidAccessToken(token: String): Unit

    /**
     * Extract the email from a refresh/access token
     */
    fun extractAccountEmail(token: String): String
}
