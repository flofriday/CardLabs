package at.tuwien.ase.cardlabs.management.amqp

import at.tuwien.ase.cardlabs.management.database.model.game.GameState
import at.tuwien.ase.cardlabs.management.mapper.GameMapper
import at.tuwien.ase.cardlabs.management.matchmaker.MatchResultQueueMessage
import at.tuwien.ase.cardlabs.management.service.bot.BotService
import at.tuwien.ase.cardlabs.management.service.game.GameService
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageListener

class MatchResultQueueRabbitMQListener(
    private val objectMapper: ObjectMapper,
    private val gameService: GameService,
    private val gameMapper: GameMapper,
    private val botService: BotService,
) : MessageListener {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun onMessage(message: Message) {
        val msg = objectMapper.readValue(message.body, object : TypeReference<MatchResultQueueMessage>() {})
        logger.debug("Received message '$msg'")

        // Update game
        val game = gameMapper.map(msg)
        game.gameState = GameState.COMPLETED
        gameService.save(game)

        // Update bots
        botService.setBotStateToDefaultState(msg.participatingBotIds)
    }
}
