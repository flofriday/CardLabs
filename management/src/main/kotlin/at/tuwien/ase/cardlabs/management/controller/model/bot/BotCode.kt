package at.tuwien.ase.cardlabs.management.controller.model.bot

/**
 * A model used to represent a version of the bot code
 */
data class BotCode(
    val id: Long?,
    val botId: Long,
    val code: String
)
