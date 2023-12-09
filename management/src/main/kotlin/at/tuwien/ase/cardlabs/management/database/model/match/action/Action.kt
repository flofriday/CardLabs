package at.tuwien.ase.cardlabs.management.database.model.match.action

import at.tuwien.ase.cardlabs.management.database.model.match.card.Card
import at.tuwien.ase.cardlabs.management.database.model.match.card.Color
import java.io.Serializable

data class Action(
    val botId: Long,
    val type: ActionType,
    val card: Card,
    val selectedColor: Color? // This field is used to store the selected color when the card type is WILD or WILD_DRAW_4
) : Serializable
