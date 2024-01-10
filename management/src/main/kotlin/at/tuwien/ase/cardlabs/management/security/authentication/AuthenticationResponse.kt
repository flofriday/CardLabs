package at.tuwien.ase.cardlabs.management.security.authentication

import java.time.Instant

/**
 * The data that is sent as response to an AuthRequest
 */
data class AuthenticationResponse(
    val refreshToken: String,
    val expiryDateRefreshToken: Instant,
    val accessToken: String,
    val expiryDateAccessToken: Instant
)
