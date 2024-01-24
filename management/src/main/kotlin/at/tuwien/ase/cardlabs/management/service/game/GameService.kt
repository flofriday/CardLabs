package at.tuwien.ase.cardlabs.management.service.game

import at.tuwien.ase.cardlabs.management.amqp.MatchQueue
import at.tuwien.ase.cardlabs.management.controller.model.game.Game
import at.tuwien.ase.cardlabs.management.controller.model.game.GameCreate
import at.tuwien.ase.cardlabs.management.database.model.game.GameDAO
import at.tuwien.ase.cardlabs.management.database.model.game.GameState
import at.tuwien.ase.cardlabs.management.database.model.game.log.LogMessage
import at.tuwien.ase.cardlabs.management.database.repository.GameRepository
import at.tuwien.ase.cardlabs.management.error.UnauthorizedException
import at.tuwien.ase.cardlabs.management.error.bot.MissingBotCodeException
import at.tuwien.ase.cardlabs.management.error.game.GameDoesNotExistException
import at.tuwien.ase.cardlabs.management.mapper.GameMapper
import at.tuwien.ase.cardlabs.management.matchmaker.Bot
import at.tuwien.ase.cardlabs.management.matchmaker.MatchQueueMessage
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.service.bot.BotService
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import kotlin.jvm.Throws

@Service
class GameService(
    private val gameRepository: GameRepository,
    private val gameMapper: GameMapper,
    private val botService: BotService,
    private val rabbitTemplate: RabbitTemplate,
    @MatchQueue private val matchQueue: Queue,
) {
    private final val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Fetch all details about a game
     */
    @Transactional
    fun fetchById(
        user: CardLabUser,
        gameId: Long,
    ): Game {
        logger.debug("User ${user.id} attempts to fetch the game $gameId")
        val game = findById(gameId)
        val botIds = botService.fetchAllBotIds(user)
        if (!game.participatingBotIds.any { it in botIds }) {
            throw UnauthorizedException("Not authorized to view this game")
        }
        return gameMapper.map(game)
    }

    /**
     * Fetch all the logs from a game
     */
    @Transactional
    fun fetchLogById(
        user: CardLabUser,
        gameId: Long,
    ): List<LogMessage> {
        logger.debug("User ${user.id} attempts to fetch the logs of the game $gameId")
        val game = findById(gameId)
        val botIds = botService.fetchAllBotIds(user)
        if (!game.participatingBotIds.any { it in botIds }) {
            throw UnauthorizedException("Not authorized to view this game")
        }
        return game.turns.flatMap { it.logMessages }
    }

    @Transactional
    @Throws(UnauthorizedException::class)
    fun fetchAllGamesWithBot(
        user: CardLabUser,
        botId: Long,
        pageable: Pageable,
    ): Page<Game> {
        logger.debug("User ${user.id} attempts to fetch all the game for bot $botId")
        if (!botService.isBotOwnedByUser(botId, user)) {
            throw UnauthorizedException("You are not authorized to view the bot $botId")
        }
        return gameRepository.findAllGamesWithBot(botId, pageable)
            .map(gameMapper::map)
    }

    /**
     * Save a game
     */
    @Transactional
    fun save(game: GameDAO): Game {
        return gameMapper.map(gameRepository.save(game))
    }

    /**
     * Save all the games
     */
    @Transactional
    fun save(games: List<GameCreate>): List<Game> {
        val gameDaos = mutableListOf<GameDAO>()
        for (game in games) {
            val dao = GameDAO()
            dao.startTime = Instant.now()
            dao.endTime = dao.startTime
            dao.winningBotId = null
            dao.disqualifiedBotId = null
            dao.turns = emptyList()
            dao.gameState = GameState.CREATED
            dao.participatingBotIds = game.participatingBotsIds
            gameDaos.add(dao)
        }
        return gameRepository.saveAll(gameDaos)
            .map(gameMapper::map)
            .toList()
    }

    /**
     * Create a match where a given bot is playing with its current code against a test bot
     *
     * @return the match id
     */
    @Transactional
    fun createTestMatch(
        user: CardLabUser,
        botId: Long,
        testBotId: Long,
        useCurrentCode: Boolean
    ): Long {
        logger.debug("User ${user.id} attempts to create a test match for the user bot $botId against the test bot $testBotId")
        val bot = botService.fetch(user, botId)
        val testBot = botService.fetchTestBotById(testBotId)

        val messageBots = mutableListOf<Bot>()
        val botLatestCode = bot.getLatestCodeVersion()

        if (useCurrentCode) {
            messageBots.add(
                Bot(
                    bot.id!!,
                    null,
                    bot.name,
                    bot.currentCode,
                )
            )
        } else {
            if (botLatestCode == null) {
                throw MissingBotCodeException("No latest code version available")
            }
            messageBots.add(
                Bot(
                    bot.id!!,
                    botLatestCode.id!!,
                    bot.name,
                    botLatestCode.code,
                )
            )
        }
        messageBots.add(
            Bot(
                testBot.id,
                null,
                bot.name,
                testBot.code
            )
        )
        val game = save(listOf(GameCreate(listOf(bot.id, testBot.id))))[0]
        val message = MatchQueueMessage(game.id!!, messageBots)
        rabbitTemplate.convertAndSend(matchQueue.name, message)
        return game.id
    }

    @Throws(GameDoesNotExistException::class)
    private fun findById(gameId: Long): GameDAO {
        return gameRepository.findByIdAndDeletedIsNull(gameId)
            ?: throw GameDoesNotExistException("A game with the id $gameId doesn't exist")
    }
}
