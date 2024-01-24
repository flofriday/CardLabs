package at.tuwien.ase.cardlabs.management.matchmaker

import at.tuwien.ase.cardlabs.management.amqp.MatchQueue
import at.tuwien.ase.cardlabs.management.config.MatchmakerConfig
import at.tuwien.ase.cardlabs.management.controller.model.bot.Bot
import at.tuwien.ase.cardlabs.management.controller.model.game.GameCreate
import at.tuwien.ase.cardlabs.management.database.model.bot.BotDAO
import at.tuwien.ase.cardlabs.management.database.repository.BotRepository
import at.tuwien.ase.cardlabs.management.error.matchmaking.InsufficientBotExistsException
import at.tuwien.ase.cardlabs.management.service.bot.BotService
import at.tuwien.ase.cardlabs.management.service.game.GameService
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.Throws

@Component
class Matchmaker(
    @Lazy private val botService: BotService,
    private val botRepository: BotRepository,
    private val matchmakerConfig: MatchmakerConfig,
    private val matchmakerAlgorithm: MatchmakerAlgorithm,
    private val gameService: GameService,
    private val rabbitTemplate: RabbitTemplate,
    @MatchQueue private val matchQueue: Queue,
) {
    private final val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    @Throws(InsufficientBotExistsException::class)
    fun createMatches(
        bot: BotDAO,
        maxMatches: Int,
    ) {
        val bots =
            botRepository.findAllBotsWithAtLeastOneBotCode()
                .filter { it.id != bot.id }
                .toList()

        if (bots.isEmpty()) {
            throw InsufficientBotExistsException("Not enough bot exists to create a match")
        }

        // Create clusters
        var clusters =
            distributeElementsIntoClusters(
                elements = bots,
                minSize = matchmakerConfig.matchSize.min,
                maxSize = matchmakerConfig.matchSize.max - 1,
            )
        for (cluster in clusters) {
            cluster.add(bot)
        }

        if (clusters.size > maxMatches) {
            clusters = clusters.subList(0, maxMatches)
        } else {
            logger.debug("Could only create ${maxMatches - clusters.size} instead of the requested $maxMatches")
        }

        // Create games
        val gameCreates = mutableListOf<GameCreate>()
        for (cluster in clusters) {
            val participatingBotIds = cluster.map { clusterBot -> clusterBot.id!! }
            gameCreates.add(GameCreate(participatingBotIds))
        }
        val gameCreateResult = gameService.save(gameCreates)

        // Send message
        for ((index, match) in clusters.withIndex()) {
            val game = gameCreateResult[index]
            val messageBots = mutableListOf<at.tuwien.ase.cardlabs.management.matchmaker.Bot>()
            for (b in match) {
                val latestCode = b.getLatestCodeVersion()
                messageBots.add(
                    Bot(
                        b.id!!,
                        latestCode!!.id!!,
                        b.name,
                        latestCode.code,
                    ),
                )
            }
            val message = MatchQueueMessage(game.id!!, messageBots)
            rabbitTemplate.convertAndSend(matchQueue.name, message)
            logger.debug("Sending the message '$message' to the queue '${matchQueue.name}'")
        }
    }

    /* @Scheduled(initialDelay = 5000L, fixedDelay = 5000L)
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
            val participatingBotIds = bots.map { bot -> bot.id!! }
            gameCreates.add(GameCreate(participatingBotIds))
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
            logger.debug("Sending the message '${message.toString()}' to the queue '${matchQueue.name}'")
        }
    }*/

    private fun distributeElementsIntoClusters(
        elements: List<BotDAO>,
        minSize: Int,
        maxSize: Int,
    ): List<MutableList<BotDAO>> {
        val clusters = mutableListOf<MutableList<BotDAO>>()
        var startIndex = 0
        var remainingElements = elements.size

        while (remainingElements > minSize) {
            val clusterSize = if (remainingElements >= maxSize) maxSize else remainingElements

            clusters.add(elements.subList(startIndex, startIndex + clusterSize).toMutableList())
            startIndex += clusterSize
            remainingElements = elements.size - startIndex
        }

        return clusters
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
}
