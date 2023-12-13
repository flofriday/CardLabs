package at.tuwien.ase.cardlabs.management.controller.model.bot

/**
 * A model used to represent the data required for bot creation
 */
data class BotCreate(
    val name: String,
    val currentCode: String? = null,
)
