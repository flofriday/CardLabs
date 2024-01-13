package simulation.models

import java.io.Serializable

data class Hand(
    val botId: Long,
    val cards: List<Card>,
) : Serializable
