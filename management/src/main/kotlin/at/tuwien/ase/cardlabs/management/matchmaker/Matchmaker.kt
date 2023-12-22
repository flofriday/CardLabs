package at.tuwien.ase.cardlabs.management.matchmaker

import at.tuwien.ase.cardlabs.management.config.MatchmakerConfig
import at.tuwien.ase.cardlabs.management.controller.model.bot.Bot
import at.tuwien.ase.cardlabs.management.controller.model.game.GameCreate
import at.tuwien.ase.cardlabs.management.database.model.bot.BotState
import at.tuwien.ase.cardlabs.management.service.bot.BotService
import at.tuwien.ase.cardlabs.management.service.game.GameService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class Matchmaker(
    private val botService: BotService,
    private val matchmakerConfig: MatchmakerConfig,
    private val matchmakerAlgorithm: MatchmakerAlgorithm,
    private val gameService: GameService,
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
            "Created ${result.matches.size} matches containing in total $assignedBots of ${queuedBots.size} queued bots with the following match sizes ${
            clusterToString(
                result.matches,
            )
            }.",
        )

        val gameCreates = mutableListOf<GameCreate>()
        val playingBotIds = result.matches.flatMap { innerList -> innerList.map { bot -> bot.id!! } }
        for (bots in result.matches) {
            gameCreates.add(GameCreate())
        }
        gameService.save(gameCreates)
        botService.updateMultipleBotState(playingBotIds, BotState.PLAYING)

        // TODO: add the matches to the RabbitMQ
    }

    private fun clusterToString(clusters: List<List<Bot>>): String {
        val builder = StringBuilder()
        builder.append("[")
        for ((index, value) in clusters.withIndex()) {
            builder.append(value.size)
            if (index < clusters.size - 1) {
                builder.append(",")
            }
        }
        builder.append("]")
        return builder.toString()
    }
}
