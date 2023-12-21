package at.tuwien.ase.cardlabs.management.controller.model.game

import at.tuwien.ase.cardlabs.management.database.model.game.GameState
import at.tuwien.ase.cardlabs.management.database.model.game.action.Action
import at.tuwien.ase.cardlabs.management.database.model.game.log.LogMessage
import at.tuwien.ase.cardlabs.management.database.model.game.result.Result
import java.time.LocalDateTime

data class Game(
    val id: Long?,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val actions: List<Action>,
    val results: List<Result>,
    val logMessages: List<LogMessage>,
    val gameState: GameState,
)
