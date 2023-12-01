package at.tuwien.ase.cardlabs.management.controller.model.bot

import at.tuwien.ase.cardlabs.management.database.model.bot.BotState

data class Bot(
    val id: Long?,
    val name: String,
    val ownerId: Long,
    val currentCode: String,
    val codeHistory: MutableList<BotCode>,
    val eloScore: Int,
    val currentState: BotState,
    val defaultState: BotState,
    val errorStateMessage: String?
)
