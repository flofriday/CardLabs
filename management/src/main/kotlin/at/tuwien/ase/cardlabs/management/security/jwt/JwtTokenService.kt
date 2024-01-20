package at.tuwien.ase.cardlabs.management.security.jwt

import at.tuwien.ase.cardlabs.management.error.authentication.InvalidTokenException
import at.tuwien.ase.cardlabs.management.error.authentication.TokenExpiredException
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.security.token.Token
import at.tuwien.ase.cardlabs.management.security.token.TokenPair
import at.tuwien.ase.cardlabs.management.security.token.TokenService
import at.tuwien.ase.cardlabs.management.security.token.TokenType
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.util.Date
import kotlin.jvm.Throws

@Service
class JwtTokenService(
    private val jwtConfig: JwtConfig
) : TokenService {

    private val claimAccountIdFieldName: String = "account-id"
    private val claimAccountUsernameFieldName: String = "account-username"
    private val claimAccountTokenTypeFieldName: String = "token-type"

    override fun generateRefreshToken(authentication: Authentication): TokenPair {
        val userDetails = authentication.principal as CardLabUser
        val refreshToken = createRefreshToken(userDetails)
        val accessToken = createAccessToken(userDetails)
        return TokenPair(refreshToken, accessToken)
    }

    override fun generateAccessToken(refreshToken: String): Token {
        // no need to check for the boolean value as it always throws an exception if invalid
        verifyValidRefreshToken(refreshToken)
        return createAccessToken(refreshToken)
    }

    override fun verifyValidRefreshToken(token: String) {
        if (isTokenExpired(token)) {
            throw TokenExpiredException("The token is expired")
        }
        if (getTokenType(token) != TokenType.REFRESH_TOKEN) {
            throw InvalidTokenException("Invalid token type")
        }
    }

    override fun verifyValidAccessToken(token: String) {
        if (isTokenExpired(token)) {
            throw TokenExpiredException("The token is expired")
        }
        if (getTokenType(token) != TokenType.ACCESS_TOKEN) {
            throw InvalidTokenException("Invalid token type")
        }
    }

    // === Token creation functions ===
    private fun createRefreshToken(cardLabUser: CardLabUser): Token {
        return createToken(
            cardLabUser.username,
            createClaim(cardLabUser.id, cardLabUser.username, TokenType.REFRESH_TOKEN),
            jwtConfig.getRefreshTokenValidity()
        )
    }

    private fun createAccessToken(refreshToken: String): Token {
        val claims = extractAllClaims(refreshToken)
        val accountId = (claims[claimAccountIdFieldName] as Int).toLong()
        val accountUsername = claims[claimAccountUsernameFieldName] as String
        return createToken(
            accountUsername,
            createClaim(accountId, accountUsername, TokenType.ACCESS_TOKEN),
            jwtConfig.getAccessTokenValidity()
        )
    }

    private fun createAccessToken(cardLabUser: CardLabUser): Token {
        return createToken(
            cardLabUser.username,
            createClaim(cardLabUser.id, cardLabUser.username, TokenType.ACCESS_TOKEN),
            jwtConfig.getAccessTokenValidity()
        )
    }

    private fun createClaim(accountId: Long, accountUsername: String, tokenType: TokenType): Map<String, Any> {
        val claims = mutableMapOf<String, Any>()
        claims[claimAccountIdFieldName] = accountId
        claims[claimAccountUsernameFieldName] = accountUsername
        claims[claimAccountTokenTypeFieldName] = tokenType
        return claims
    }

    private fun createToken(subject: String, claims: Map<String, Any>, validity: Duration): Token {
        val now = Instant.now()
        val expiry = now.plus(validity)

        val token = Jwts.builder()
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expiry))
            .setSubject(subject)
            .addClaims(claims)
            .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecretKey())
            .compact()
        return Token(token, expiry)
    }

    // === Token extract information functions ===
    /**
     * Check if the token is expired, if the token expiration is null, then an exception is thrown
     */
    @Throws(InvalidTokenException::class)
    private fun isTokenExpired(token: String): Boolean {
        val claims = extractAllClaims(token)
        if (claims.expiration == null) {
            throw InvalidTokenException("The token expiration is not set")
        }
        return !Instant.now().isBefore(claims.expiration.toInstant())
    }

    /**
     * Get the token type of a token, if the token type is null, empty or invalid then an InvalidTokenException is
     * thrown
     */
    @Throws(InvalidTokenException::class)
    private fun getTokenType(token: String): TokenType {
        val claims = extractAllClaims(token)
        if (claims[claimAccountTokenTypeFieldName] == null) {
            throw InvalidTokenException("The token type is not set")
        }
        val tokenType = claims[claimAccountTokenTypeFieldName] as String
        try {
            return TokenType.valueOf(tokenType)
        } catch (exception: IllegalArgumentException) {
            throw InvalidTokenException("The token type $tokenType is unknown")
        }
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(jwtConfig.getSecretKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    override fun extractAccountUsername(token: String): String {
        val claims = extractAllClaims(token)
        return claims[claimAccountUsernameFieldName] as String
    }
}
