package at.tuwien.ase.cardlabs.management.amqp

import at.tuwien.ase.cardlabs.management.database.model.game.GameState
import at.tuwien.ase.cardlabs.management.mapper.GameMapper
import at.tuwien.ase.cardlabs.management.matchmaker.MatchResultQueueMessage
import at.tuwien.ase.cardlabs.management.matchmaker.calculateScore
import at.tuwien.ase.cardlabs.management.service.bot.BotService
import at.tuwien.ase.cardlabs.management.service.game.GameService
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageListener
import org.springframework.transaction.annotation.Transactional

open class MatchResultQueueListener(
    private val objectMapper: ObjectMapper,
    private val gameService: GameService,
    private val gameMapper: GameMapper,
    private val botService: BotService,
) : MessageListener {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun onMessage(message: Message) {
        val msg = objectMapper.readValue(message.body, object : TypeReference<MatchResultQueueMessage>() {})
        logger.debug("Received message '$msg'")

        // Update game
        val game = gameMapper.map(msg)
        game.gameState = GameState.COMPLETED
        gameService.save(game)

        // Update bot states
        botService.setBotStateToDefaultState(msg.participatingBotIds)

        val containsTestBot = msg.participatingBotIds.any { it < 0 }
        // If a test bot is in a match then no score or ban state should be updated
        if (containsTestBot) {
            return
        }
        // Ban bot if disqualified
        if (msg.disqualifiedBotId != null) {
            botService.updateBanState(msg.disqualifiedBotId, true)
        }

        // Update bot elo
        val bots = botService.fetchBotsByIds(msg.participatingBotIds)
        val winningBot = bots.find { it.id!! == msg.winningBotId }
        bots.forEach { bot ->
            run {
                val newEloScore = calculateScore(
                    bot,
                    if (winningBot == null) false else bot.id!! == winningBot.id!!,
                    bots,
                )
                logger.debug("Updating the elo score for bot ${bot.id} from ${bot.eloScore} to $newEloScore")
                botService.updateEloScore(bot.id!!, newEloScore)
            }
        }
    }
}
