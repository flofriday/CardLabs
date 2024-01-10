package at.tuwien.ase.cardlabs.management.controller.model.game

import at.tuwien.ase.cardlabs.management.database.model.game.GameState
import at.tuwien.ase.cardlabs.management.database.model.game.turn.Turn
import java.time.Instant

data class Game(
    val id: Long?,
    val startTime: Instant,
    val endTime: Instant,
    val winningBotId: Long?,
    val disqualifiedBotId: Long?,
    val turns: List<Turn>,
    val gameState: GameState,
)
