package at.tuwien.ase.cardlabs.management.matchmaker

import java.io.Serializable

/**
 * The message that is sent to RabbitMQ when a match has been created and is awaiting execution
 */
data class MatchQueueMessage(
    val gameId: Long,
    val participatingBots: List<Bot>
) : Serializable

data class Bot(
    val botId: Long,
    val botCodeId: Long,
    val code: String,
) : Serializable