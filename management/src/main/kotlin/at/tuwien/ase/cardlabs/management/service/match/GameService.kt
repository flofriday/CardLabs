package at.tuwien.ase.cardlabs.management.service.match

import at.tuwien.ase.cardlabs.management.controller.model.game.Game
import at.tuwien.ase.cardlabs.management.database.model.match.GameDAO
import at.tuwien.ase.cardlabs.management.database.model.match.log.LogMessage
import at.tuwien.ase.cardlabs.management.database.repository.GameRepository
import at.tuwien.ase.cardlabs.management.error.game.GameDoesNotExistException
import at.tuwien.ase.cardlabs.management.mapper.GameMapper
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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
        return game.logMessages
    }

    private fun findById(gameId: Long): GameDAO {
        return gameRepository.findByIdAndDeletedIsNull(gameId)
            ?: throw GameDoesNotExistException("A game with the id $gameId doesn't exist")
    }
}
