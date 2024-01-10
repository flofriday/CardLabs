package at.tuwien.ase.cardlabs.management.security.authentication

/**
 * The data that is sent in order to request a refresh token
 */
data class AuthRequest(val username: String, val password: String)
