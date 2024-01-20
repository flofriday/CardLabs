package at.tuwien.ase.cardlabs.management.matchmaker

import at.tuwien.ase.cardlabs.management.database.model.game.action.ActionType
import at.tuwien.ase.cardlabs.management.database.model.game.card.CardType
import at.tuwien.ase.cardlabs.management.database.model.game.card.Color
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.io.Serializable
import java.time.Instant

/**
 * The message that is sent to RabbitMQ when a match has been completed
 */
data class MatchResultQueueMessage(
    val gameId: Long,
    val startTime: Instant,
    val endTime: Instant,
    val winningBotId: Long?,
    val disqualifiedBotId: Long?,
    val turns: List<Turn>,
    val participatingBotIds: List<Long>,
) : Serializable

data class Turn(
    val turnId: Long,
    val activeBotId: Long,
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
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
)
@JsonSubTypes(
    JsonSubTypes.Type(value = SystemLogMessage::class, name = "system"),
    JsonSubTypes.Type(value = DebugLogMessage::class, name = "debug"),
)
open class LogMessage(
    val message: String,
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LogMessage

        return message == other.message
    }

    override fun hashCode(): Int {
        return message.hashCode()
    }
}

class DebugLogMessage(
    message: String,
    val botId: Long,
) : LogMessage(message) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as DebugLogMessage

        return botId == other.botId
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + botId.hashCode()
        return result
    }
}

class SystemLogMessage(
    message: String,
) : LogMessage(message) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}

data class Card(
    val type: CardType,
    val color: Color,
    val number: Int?,
) : Serializable
