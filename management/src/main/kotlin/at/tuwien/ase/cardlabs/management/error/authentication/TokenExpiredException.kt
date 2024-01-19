package at.tuwien.ase.cardlabs.management.error.authentication

/**
 * An exception that is thrown when attempting to use an expired token
 */
class TokenExpiredException(message: String?) : Exception(message)
