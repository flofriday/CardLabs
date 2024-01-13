package simulation.models

import java.io.Serializable

enum class ActionType {
    DRAW_CARD,
    PLAY_CARD,
}

data class Action(
    val botId: Long,
    val type: ActionType,
    val card: Card,
) : Serializable
