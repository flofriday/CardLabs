package at.tuwien.ase.cardlabs.management.error.authentication

/**
 * An exception that is thrown when attempting to generate a refresh token
 */
class RefreshTokenExpiredException(message: String?) : Exception(message)
