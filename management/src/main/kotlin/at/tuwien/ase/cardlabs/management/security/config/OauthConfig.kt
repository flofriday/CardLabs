package at.tuwien.ase.cardlabs.management.security.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "cardlabs.oauth", ignoreInvalidFields = true)
data class OauthConfig(
    val frontend: String,
)
