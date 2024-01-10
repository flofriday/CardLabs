package at.tuwien.ase.cardlabs.management.security.authentication

import java.time.Instant

/**
 * The data that is sent as response to a RefreshTokenRequest
 */
data class AccessTokenAuthenticationResponse(val accessToken: String, val expiryDate: Instant)
