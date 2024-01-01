package at.tuwien.ase.cardlabs.management.matchmaker

import at.tuwien.ase.cardlabs.management.database.model.game.action.ActionType
import at.tuwien.ase.cardlabs.management.database.model.game.card.CardType
import at.tuwien.ase.cardlabs.management.database.model.game.card.Color
import java.io.Serializable
import java.time.LocalDateTime

/**
 * The message that is sent to RabbitMQ when a match has been completed
 */
data class MatchResultQueueMessage(
    val gameId: Long,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val winningBotId: Long,
    val rounds: List<Round>,
    val participatingBots: List<Long>,
) : Serializable

data class Round(
    val roundId: Long,
    val topCard: Card,
    val drawPile: List<Card>, // Stores the top 10 cards of the pile, fewer if there a fewer on the pile
    val hands: List<Hand>,
    val actions: List<Action>,
    val logMessages: List<LogMessage>,
) : Serializable

data class Hand(
    val botId: Long,
    val cards: List<Card>,
) : Serializable

data class Action(
    val botId: Long,
    val type: ActionType,
    val card: Card,
) : Serializable

// === Log messages ===
open class LogMessage(
    val message: String,
) : Serializable

class DebugLogMessage(
    message: String,
    val botId: Long,
) : LogMessage(message)

class SystemLogMessage(
    message: String,
) : LogMessage(message)

data class Card(
    val type: CardType,
    val color: Color,
    val number: Int?,
) : Serializable
