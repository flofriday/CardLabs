package at.tuwien.ase.cardlabs.management.database.model.game.round

import at.tuwien.ase.cardlabs.management.database.model.game.action.Action
import at.tuwien.ase.cardlabs.management.database.model.game.card.Card
import at.tuwien.ase.cardlabs.management.database.model.game.hand.Hand
import at.tuwien.ase.cardlabs.management.database.model.game.log.LogMessage
import java.io.Serializable

data class Turn(
    val roundId: Long,
    val topCard: Card,
    val drawPile: List<Card>, // Stores the top 10 cards of the pile, fewer if their are fewer on the pile
    val hands: List<Hand>,
    val actions: List<Action>,
    val logMessages: List<LogMessage>,
) : Serializable
