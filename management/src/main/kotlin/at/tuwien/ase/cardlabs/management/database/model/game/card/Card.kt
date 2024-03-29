package at.tuwien.ase.cardlabs.management.database.model.game.card

import java.io.Serializable

data class Card(
    val type: CardType,
    val color: Color,
    val number: Int?,
) : Serializable
