package at.tuwien.ase.cardlabs.management.matchmaker

import at.tuwien.ase.cardlabs.management.amqp.MatchQueue
import at.tuwien.ase.cardlabs.management.config.MatchmakerConfig
import at.tuwien.ase.cardlabs.management.controller.model.bot.Bot
import at.tuwien.ase.cardlabs.management.controller.model.game.GameCreate
import at.tuwien.ase.cardlabs.management.database.model.bot.BotState
import at.tuwien.ase.cardlabs.management.service.bot.BotService
import at.tuwien.ase.cardlabs.management.service.game.GameService
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class Matchmaker(
    private val botService: BotService,
    private val matchmakerConfig: MatchmakerConfig,
    private val matchmakerAlgorithm: MatchmakerAlgorithm,
    private val gameService: GameService,
    private val rabbitTemplate: RabbitTemplate,
    @MatchQueue private val matchQueue: Queue
) {

    private final val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(initialDelay = 5000L, fixedDelay = 5000L)
    @Transactional
    fun scheduleFixedDelayTask() {
        logger.debug("Creating matches")
        val queuedBots = botService.fetchByState(BotState.QUEUED)
        logger.debug("Fetched ${queuedBots.size} bots that are waiting for a match")

        // Check if the minimum number of required bots is present
        if (queuedBots.size < matchmakerConfig.matchSize.min) {
            logger.debug("${queuedBots.size} are waiting for a match, insufficient amount to create a match.")
            return
        }

        val result = matchmakerAlgorithm.createMatches(queuedBots)
        val assignedBots = queuedBots.size - result.unassignedBots.size
        logger.debug(
            "Creating ${result.matches.size} matches containing in total $assignedBots of ${queuedBots.size} queued bots with the following matches ${
            clusterToString(
                result.matches,
            )
            }.",
        )

        // Create games in database
        val gameCreates = mutableListOf<GameCreate>()
        val playingBotIds = result.matches.flatMap { innerList -> innerList.map { bot -> bot.id!! } }
        for (bots in result.matches) {
            gameCreates.add(GameCreate())
        }
        val gameCreateResult = gameService.save(gameCreates)

        // Update bot state
        botService.updateMultipleBotState(playingBotIds, BotState.PLAYING)

        // Send matches to RabbitMQ
        for ((index, match) in result.matches.withIndex()) {
            val game = gameCreateResult[index]
            val messageBots = mutableListOf<at.tuwien.ase.cardlabs.management.matchmaker.Bot>()
            for (bot in match) {
                val latestCode = bot.getLatestCodeVersion()
                messageBots.add(
                    Bot(
                        bot.id!!,
                        latestCode!!.id!!,
                        latestCode.code,
                    )
                )
            }
            val message = MatchQueueMessage(game.id!!, messageBots)
            rabbitTemplate.convertAndSend(matchQueue.name, message)
            logger.debug("Sending the message '${matchQueueMessageToString(message)}' to the queue '${matchQueue.name}'")
        }
    }

    private fun clusterToString(clusters: List<List<Bot>>): String {
        val builder = StringBuilder()
        builder.append("[")
        for ((index, value) in clusters.withIndex()) {
            builder
                .append("[")
                .append(value.map { it.id }.joinToString(", "))
                .append("]")
            if (index < clusters.size - 1) {
                builder.append(",")
            }
        }
        builder.append("]")
        return builder.toString()
    }

    private fun matchQueueMessageToString(message: MatchQueueMessage): String {
        val builder = StringBuilder()
        builder
            .append("(")
            .append("gameId=")
            .append(message.gameId)
            .append(",")
        builder.append("bots=[")
        for ((index, bot) in message.participatingBots.withIndex()) {
            builder
                .append("id=").append(bot.botId)
                .append(",")
                .append("codeId=").append(bot.botCodeId)
            if (index < message.participatingBots.size - 1) {
                builder.append(",")
            }
        }
        builder
            .append("]")
            .append(")")
        return builder.toString()
    }
}
