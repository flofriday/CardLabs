package at.tuwien.ase.cardlabs.management.controller.model.game

import at.tuwien.ase.cardlabs.management.database.model.match.action.Action
import at.tuwien.ase.cardlabs.management.database.model.match.log.LogMessage
import at.tuwien.ase.cardlabs.management.database.model.match.result.Result
import java.time.LocalDateTime

data class Game(
    val id: Long?,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val actions: List<Action>,
    val results: List<Result>,
    val logMessages: List<LogMessage>,
)
