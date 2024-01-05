package simulation.models

import java.io.Serializable
import java.time.Instant

class SimulationResult(
    val gameId: Long,
    val startTime: Instant,
    val endTime: Instant?,
    val winningBotId: Long?,
    val disqualifiedBotId: Long?,
    val turns: List<Turn>,
    val participatingBotsIds: List<Long>
) : Serializable