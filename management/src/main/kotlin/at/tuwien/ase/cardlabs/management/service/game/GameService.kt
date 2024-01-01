package at.tuwien.ase.cardlabs.management.service.game

import at.tuwien.ase.cardlabs.management.controller.model.game.Game
import at.tuwien.ase.cardlabs.management.controller.model.game.GameCreate
import at.tuwien.ase.cardlabs.management.database.model.game.GameDAO
import at.tuwien.ase.cardlabs.management.database.model.game.GameState
import at.tuwien.ase.cardlabs.management.database.model.game.log.LogMessage
import at.tuwien.ase.cardlabs.management.database.repository.GameRepository
import at.tuwien.ase.cardlabs.management.error.game.GameDoesNotExistException
import at.tuwien.ase.cardlabs.management.mapper.GameMapper
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class GameService(
    private val gameRepository: GameRepository,
    private val gameMapper: GameMapper,
) {

    private final val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Fetch all details about a game
     */
    @Transactional
    fun fetchById(user: CardLabUser, gameId: Long): Game {
        logger.debug("User ${user.id} attempts to fetch the game $gameId")
        val game = findById(gameId)
        return gameMapper.map(game)
    }

    /**
     * Fetch all the logs from a game
     */
    @Transactional
    fun fetchLogById(user: CardLabUser, gameId: Long): List<LogMessage> {
        logger.debug("User ${user.id} attempts to fetch the logs of the game $gameId")
        val game = findById(gameId)
        return game.rounds.flatMap { it.logMessages }
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
            dao.startTime = LocalDateTime.now()
            dao.endTime = dao.startTime
            dao.winningBotId = null
            dao.rounds = emptyList()
            dao.gameState = GameState.CREATED
            gameDaos.add(dao)
        }
        return gameRepository.saveAll(gameDaos)
            .map(gameMapper::map)
            .toList()
    }

    private fun findById(gameId: Long): GameDAO {
        return gameRepository.findByIdAndDeletedIsNull(gameId)
            ?: throw GameDoesNotExistException("A game with the id $gameId doesn't exist")
    }
}
