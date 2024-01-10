package at.tuwien.ase.cardlabs.management.mapper

import at.tuwien.ase.cardlabs.management.matchmaker.Action
import at.tuwien.ase.cardlabs.management.matchmaker.Card
import at.tuwien.ase.cardlabs.management.matchmaker.Hand
import at.tuwien.ase.cardlabs.management.matchmaker.Turn
import org.springframework.stereotype.Component

@Component
class TurnMapper(
    private val logMessageMapper: LogMessageMapper,
) {

    fun mapRound(turn: Turn): at.tuwien.ase.cardlabs.management.database.model.game.turn.Turn {
        return at.tuwien.ase.cardlabs.management.database.model.game.turn.Turn(
            turnId = turn.turnId,
            topCard = mapCard(turn.topCard),
            drawPile = turn.drawPile.map(::mapCard).toList(),
            hands = turn.hands.map(::mapHand).toList(),
            actions = turn.actions.map(::mapAction).toList(),
            logMessages = turn.logMessages.map(logMessageMapper::mapLogMessage).toList(),
        )
    }

    private fun mapCard(card: Card): at.tuwien.ase.cardlabs.management.database.model.game.card.Card {
        return at.tuwien.ase.cardlabs.management.database.model.game.card.Card(
            type = card.type,
            color = card.color,
            number = card.number
        )
    }

    private fun mapHand(hand: Hand): at.tuwien.ase.cardlabs.management.database.model.game.hand.Hand {
        return at.tuwien.ase.cardlabs.management.database.model.game.hand.Hand(
            botId = hand.botId,
            cards = hand.cards.map(::mapCard).toList()
        )
    }

    private fun mapAction(action: Action): at.tuwien.ase.cardlabs.management.database.model.game.action.Action {
        return at.tuwien.ase.cardlabs.management.database.model.game.action.Action(
            botId = action.botId,
            type = action.type,
            card = mapCard(action.card)
        )
    }
}
