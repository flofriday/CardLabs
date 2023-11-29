package at.tuwien.ase.cardlabs.management.controller.model.bot

data class BotCreate(
    val name: String,
    val currentCode: String? = null
)
