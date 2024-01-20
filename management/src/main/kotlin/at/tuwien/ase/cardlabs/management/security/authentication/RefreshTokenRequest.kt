package at.tuwien.ase.cardlabs.management.security.authentication

/**
 * The data that is sent in order to request an access token
 */
data class RefreshTokenRequest(val refreshToken: String)
