package at.tuwien.ase.cardlabs.management.database.model.game.hand

import at.tuwien.ase.cardlabs.management.database.model.game.card.Card
import java.io.Serializable

data class Hand(
    val botId: Long,
    val cards: List<Card>
) : Serializable
