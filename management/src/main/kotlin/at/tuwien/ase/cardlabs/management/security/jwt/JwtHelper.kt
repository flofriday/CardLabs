package at.tuwien.ase.cardlabs.management.security.jwt

import at.tuwien.ase.cardlabs.management.security.CardLabUser
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.Authentication
import java.util.Date

class JwtHelper {

    companion object {

        lateinit var secretKey: String

        @JvmStatic
        fun generateToken(authentication: Authentication): String {
            val userDetails = authentication.principal as CardLabUser
            val now = Date()
            val validityDuration = 12 * 60 * 60 * 1000; // 12 hours
            val expiryDate = Date(now.time + validityDuration)

            return Jwts.builder()
                .setSubject(authentication.name)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("account_id", userDetails.id)
                .claim("account_username", userDetails.username)
                .claim("account_email", userDetails.email)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact()
        }

        @JvmStatic
        fun validateToken(token: String): Boolean {
            val claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .body

            return !isTokenExpired(claims)
        }

        @JvmStatic
        fun getUsernameFromToken(token: String): String {
            return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .body
                .subject
        }

        @JvmStatic
        private fun isTokenExpired(claims: Claims): Boolean {
            val expiration: Date = claims.expiration
            return expiration.before(Date())
        }
    }
}
