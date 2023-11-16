package at.tuwien.ase.cardlabs.management.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.Authentication
import java.util.*


class JwtHelper {

    companion object {

        //        @Value("\${jwt.secret}")
        private val secretKey =
            "2e0377d9c56d8a51ed8cfc10a68bda5b3cdc7453a6c7b6ac4728d93e052618051bba4534b146e3cff10bed31224793cb46f78a628b8d6dc08b1b5496a05cf488"

        fun generateToken(authentication: Authentication): String {
            val now = Date()
            val validityDuration = 12 * 60 * 60 * 1000; // 12 hours
            val expiryDate = Date(now.time + validityDuration)

            return Jwts.builder()
                .setSubject(authentication.name)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                // TODO: add account_Id
                .claim("account_username", authentication.name)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact()
        }

        fun validateToken(token: String): Boolean {
            val claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .body

            return !isTokenExpired(claims)
        }

        fun getUsernameFromToken(token: String): String {
            return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .body
                .subject
        }

        private fun isTokenExpired(claims: Claims): Boolean {
            val expiration: Date = claims.expiration
            return expiration.before(Date())
        }

    }

}