package at.tuwien.ase.cardlabs.management.controller.model.game

import at.tuwien.ase.cardlabs.management.database.model.game.GameState
import at.tuwien.ase.cardlabs.management.database.model.game.round.Round
import java.time.LocalDateTime

data class Game(
    val id: Long?,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val winningBotId: Long?,
    val rounds: List<Round>,
    val gameState: GameState,
)
