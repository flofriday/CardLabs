package at.tuwien.ase.cardlabs.management.error.account

/**
 * An exception that is thrown when attempting to perform an operation on an account that does not exist
 */
class AccountDoesNotExistException(message: String?) : Exception(message)
