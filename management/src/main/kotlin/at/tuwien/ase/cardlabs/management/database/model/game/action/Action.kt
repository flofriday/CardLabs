package at.tuwien.ase.cardlabs.management.database.model.game.action

import at.tuwien.ase.cardlabs.management.database.model.game.card.Card
import java.io.Serializable

data class Action(
    val botId: Long,
    val type: ActionType,
    val card: Card,
) : Serializable
