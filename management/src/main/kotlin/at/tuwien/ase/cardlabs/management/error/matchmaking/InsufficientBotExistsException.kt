package at.tuwien.ase.cardlabs.management.error.matchmaking

/**
 * An exception that is thrown when attempting to create a match and not sufficient bots are present
 */
class InsufficientBotExistsException(message: String?) : Exception(message)
