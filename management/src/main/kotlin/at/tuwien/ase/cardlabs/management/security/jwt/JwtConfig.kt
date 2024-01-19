package at.tuwien.ase.cardlabs.management.security.jwt

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class JwtConfig {

    @Value("\${jwt.secret}")
    private lateinit var secretKey: String

    @Value("\${jwt.refresh-token-validity}")
    private var refreshTokenValidity: Long? = null // In s

    @Value("\${jwt.access-token-validity}")
    private var accessTokenValidity: Long? = null // In s

    fun getSecretKey(): String {
        return secretKey
    }

    fun getRefreshTokenValidity(): Duration {
        return Duration.ofSeconds(refreshTokenValidity!!)
    }

    fun getAccessTokenValidity(): Duration {
        return Duration.ofSeconds(accessTokenValidity!!)
    }
}
