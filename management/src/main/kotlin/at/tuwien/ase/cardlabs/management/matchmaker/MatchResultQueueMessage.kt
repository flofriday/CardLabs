package at.tuwien.ase.cardlabs.management.matchmaker

import java.io.Serializable
import java.time.LocalDateTime


/**
 * The message that is sent to RabbitMQ when a match has been created and is awaiting execution
 */
data class MatchResultQueueMessage(
    val gameId: Long,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val winningBotId: Long,
    val logMessages: List<LogMessage>,
    val initialHand: List<Hand>,
    val actions: List<Action>,
) : Serializable

// === Log Messages ===
open class LogMessage(
    val move: Long,
    val message: String,
) : Serializable

class SystemLogMessage(
    move: Long,
    message: String,
) : LogMessage(move, message)

class DebugLogMessage(
    move: Long,
    message: String,
    val botId: Long,
) : LogMessage(move, message)

// === Hand ===
data class Hand(
    val botId: Long,
    val cards: List<Card>
): Serializable

// === Action ===
data class Action(
    val botId: Long,
    val move: Long,
    val type: ActionType,
    val card: Card,
    val selectedColor: Color?, // This field is used to store the selected color when the card type is WILD or WILD_DRAW_4
) : Serializable

enum class ActionType {
    INITIAL_CARD_DRAW,
    DRAW_CARD,
    PLAY_CARD,
}

// === Card ===
data class Card(
    val type: CardType,
    val color: Color?,  // This field is used to store the card color when the card type is NUMBER, REVERSE, SKIP or DRAW
    val number: Int?,
) : Serializable

enum class CardType {
    NUMBER,
    REVERSE,
    SKIP,
    DRAW,
    WILD,
    WILD_DRAW_4,
}

enum class Color {
    CYAN,
    ORANGE,
    GREEN,
    PURPLE,
}