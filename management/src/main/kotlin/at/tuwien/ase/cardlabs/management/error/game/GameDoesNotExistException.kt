package at.tuwien.ase.cardlabs.management.error.game

/**
 * An exception that is thrown when attempting to perform an operation on a game that does not exist
 */
class GameDoesNotExistException(message: String?) : Exception(message)
