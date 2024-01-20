package at.tuwien.ase.cardlabs.management.security.token

import java.time.Instant

data class Token(
    val token: String,
    val expiration: Instant
)
