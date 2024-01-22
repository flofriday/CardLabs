package at.tuwien.ase.cardlabs.management.matchmaker

import java.io.Serializable

/**
 * The message that is sent to RabbitMQ when a match has been created and is awaiting execution
 */
data class MatchQueueMessage(
    val gameId: Long,
    val participatingBots: List<Bot>
) : Serializable {

    override fun toString(): String {
        val builder = StringBuilder()
        builder
            .append("(")
            .append("gameId=")
            .append(gameId)
            .append(",")
        builder.append("bots=[")
        for ((index, bot) in participatingBots.withIndex()) {
            builder
                .append("id=").append(bot.botId)
                .append(",")
                .append("codeId=").append(bot.botCodeId)
            if (index < participatingBots.size - 1) {
                builder.append(",")
            }
        }
        builder
            .append("]")
            .append(")")
        return builder.toString()
    }
}

data class Bot(
    val botId: Long,
    val botCodeId: Long?,
    val code: String,
) : Serializable
