package simulation.models

import java.io.Serializable
import java.time.LocalDateTime

class SimulationResult(
    val gameId: Long,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime?,
    val winningBotId: Long?,
    val disqualifiedBotId: Long?,
    val turns: List<Turn>,
    val participatingBotsIds: List<Long>
) : Serializable