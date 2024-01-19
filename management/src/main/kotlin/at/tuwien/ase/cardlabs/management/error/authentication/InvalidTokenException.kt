package at.tuwien.ase.cardlabs.management.error.authentication

/**
 * An exception that is thrown when attempting to use an invalid token such as a refresh token for access token
 * generation
 */
class InvalidTokenException(message: String?) : Exception(message)
