package at.tuwien.ase.cardlabs.management.security.jwt

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfig {

    @Value("\${jwt.secret}")
    private lateinit var secretKey: String

    @PostConstruct
    fun init() {
        JwtHelper.secretKey = secretKey
    }
}
