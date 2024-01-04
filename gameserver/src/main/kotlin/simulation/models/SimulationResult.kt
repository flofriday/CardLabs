package simulation.models

import com.fasterxml.jackson.annotation.JsonFormat
import java.io.Serializable
import java.time.LocalDateTime

class SimulationResult(
    val gameId: Long,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    val startTime: LocalDateTime,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    val endTime: LocalDateTime?,
    val winningBotId: Long?,
    val disqualifiedBotId: Long?,
    val turns: List<Turn>,
    val participatingBotsIds: List<Long>
) : Serializable