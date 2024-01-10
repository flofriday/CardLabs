package at.tuwien.ase.cardlabs.management.security.jwt

import at.tuwien.ase.cardlabs.management.error.authentication.RefreshTokenExpiredException
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.Authentication
import java.time.Duration
import java.util.Date

class JwtHelper {

    companion object {

        lateinit var secretKey: String

        lateinit var refreshTokenValidity: Duration

        lateinit var accessTokenValidity: Duration

        @JvmStatic
        fun extractSubject(token: String): String {
            return extractClaim(token, Claims::getSubject)
        }

        @JvmStatic
        fun extractAccountId(token: String): Long {
            // Claims only support int
            return (extractAllClaims(token)["account_id"] as Int).toLong()
        }

        @JvmStatic
        fun extractAccountUsername(token: String): String {
            return extractAllClaims(token)["account_username"] as String
        }

        @JvmStatic
        fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
            return claimsResolver(extractAllClaims(token))
        }

        @JvmStatic
        private fun isTokenExpired(claims: Claims): Boolean {
            return claims.expiration.before(Date())
        }

        @JvmStatic
        private fun extractAllClaims(token: String): Claims {
            return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .body
        }

        @JvmStatic
        fun generateRefreshToken(authentication: Authentication): JwtToken {
            val userDetails = authentication.principal as CardLabUser
            val claims = mutableMapOf<String, Any>()
            claims["account_id"] = userDetails.id
            claims["account_username"] = userDetails.username
            return createToken(userDetails.username, claims, refreshTokenValidity)
        }

        @JvmStatic
        fun generateAccessTokenFromRefreshToken(token: String): JwtToken {
            if (!isValidToken(token)) {
                throw RefreshTokenExpiredException("The JWT refresh token has expired")
            }

            val accountId = extractAccountId(token)
            val accountUsername = extractAccountUsername(token)
            return generateAccessToken(accountId, accountUsername)
        }

        @JvmStatic
        private fun generateAccessToken(accountId: Long, username: String): JwtToken {
            val claims = mutableMapOf<String, Any>()
            claims["account_id"] = accountId
            claims["account_username"] = username
            return createToken(username, claims, accessTokenValidity)
        }

        @JvmStatic
        private fun createToken(subject: String, claims: Map<String, Any>, validityDuration: Duration): JwtToken {
            val now = Date()
            val expiryDate = Date(now.time + validityDuration.toMillis())

            val token = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact()
            return JwtToken(token, expiryDate.toInstant())
        }

        @JvmStatic
        fun isValidToken(token: String): Boolean {
            return !isTokenExpired(extractAllClaims(token))
        }
    }
}
