package at.tuwien.ase.cardlabs.management.matchmaker

import at.tuwien.ase.cardlabs.management.config.MatchmakerConfig
import at.tuwien.ase.cardlabs.management.database.model.bot.BotState
import at.tuwien.ase.cardlabs.management.service.bot.BotService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class Matchmaker(
    private val botService: BotService,
    private val matchmakerConfig: MatchmakerConfig,
) {

    private final val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(initialDelay = 5000L, fixedDelay = 5000L)
    fun scheduleFixedDelayTask() {
        logger.debug("Creating matches")
        val queuedBots = botService.fetchByState(BotState.QUEUED)
        logger.debug("Fetched ${queuedBots.size} bots that are in the state Queued")

        // Check if the minimum amount of required bots are present
        if (queuedBots.size < matchmakerConfig.matchSize.min) {
            logger.debug("${queuedBots.size} are waiting for a match, insufficient amount to create a match. Minimum match size ${matchmakerConfig.matchSize.min}")
            return
        }
    }
}
