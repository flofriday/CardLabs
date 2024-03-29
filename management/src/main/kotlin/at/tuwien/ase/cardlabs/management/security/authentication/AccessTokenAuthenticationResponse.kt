package at.tuwien.ase.cardlabs.management.security.authentication

import at.tuwien.ase.cardlabs.management.security.token.Token

/**
 * The data that is sent as response to a RefreshTokenRequest
 */
data class AccessTokenAuthenticationResponse(val token: Token)
