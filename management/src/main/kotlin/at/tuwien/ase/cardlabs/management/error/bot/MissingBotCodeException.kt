package at.tuwien.ase.cardlabs.management.error.bot

/**
 * An exception that is thrown when attempting to access the bots current code or a specific bot code version that is
 * not available
 */
class MissingBotCodeException(message: String?) : Exception(message)
