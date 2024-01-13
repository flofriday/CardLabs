package simulation.models

import java.io.Serializable

data class Turn(
    val turnId: Long,
    val topCard: Card,
    val drawPile: List<Card>,
    val hands: List<Hand>,
    val actions: List<Action>,
    val logMessages: List<LogMessage>,
) : Serializable
