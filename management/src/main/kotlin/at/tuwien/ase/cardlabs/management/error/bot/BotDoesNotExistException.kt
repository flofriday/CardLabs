package at.tuwien.ase.cardlabs.management.error.bot

/**
 * An exception that is thrown when attempting to perform an operation on a bot that does not exist
 */
class BotDoesNotExistException(message: String?) : Exception(message)
