package at.tuwien.ase.cardlabs.management.security.authentication

import at.tuwien.ase.cardlabs.management.security.token.Token

/**
 * The data that is sent as response to an AuthRequest
 */
data class AuthenticationResponse(
    val refreshToken: Token,
    val accessToken: Token
)
