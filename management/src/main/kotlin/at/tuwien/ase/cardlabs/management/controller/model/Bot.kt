package at.tuwien.ase.cardlabs.management.controller.model

import at.tuwien.ase.cardlabs.management.database.model.BotState

data class Bot(
    val id: Long?,
    val name: String,
    val ownerId: Long,
    val code: String,
    val eloScore: Int,
    val currentState: BotState,
    val defaultState: BotState,
    val errorStateMessage: String
)
