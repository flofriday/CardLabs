package at.tuwien.ase.cardlabs.management.security.jwt

import java.time.Instant

data class JwtToken(val token: String, val expiryDate: Instant)
