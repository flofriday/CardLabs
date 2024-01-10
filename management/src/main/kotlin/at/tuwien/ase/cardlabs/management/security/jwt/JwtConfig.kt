package at.tuwien.ase.cardlabs.management.security.jwt

import jakarta.annotation.PostConstruct
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

    @PostConstruct
    fun init() {
        JwtHelper.secretKey = secretKey
        JwtHelper.refreshTokenValidity = Duration.ofSeconds(refreshTokenValidity!!)
        JwtHelper.accessTokenValidity = Duration.ofSeconds(accessTokenValidity!!)
    }
}
