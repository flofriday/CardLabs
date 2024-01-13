package simulation.models

import java.io.Serializable

data class SimulationRequest(
    val gameId: Long,
    val participatingBots: List<Bot>,
) : Serializable
