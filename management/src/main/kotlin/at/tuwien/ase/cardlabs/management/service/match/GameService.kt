package at.tuwien.ase.cardlabs.management.service.match

import at.tuwien.ase.cardlabs.management.controller.model.game.Game
import at.tuwien.ase.cardlabs.management.database.model.match.GameDAO
import at.tuwien.ase.cardlabs.management.database.model.match.log.LogMessage
import at.tuwien.ase.cardlabs.management.database.repository.GameRepository
import at.tuwien.ase.cardlabs.management.error.game.GameDoesNotExistException
import at.tuwien.ase.cardlabs.management.mapper.GameMapper
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GameService(
    private val gameRepository: GameRepository,
    private val gameMapper: GameMapper
) {

    @Transactional
    fun fetchAllById(user: CardLabUser, gameId: Long): Game {
        val game = findById(gameId)
        return gameMapper.map(game)
    }

    @Transactional
    fun fetchLogById(user: CardLabUser, gameId: Long): List<LogMessage> {
        val game = findById(gameId)
        return game.logMessages
    }

    private fun findById(gameId: Long): GameDAO {
        return gameRepository.findByIdAndDeletedIsNull(gameId)
            ?: throw GameDoesNotExistException("A game with the id $gameId doesn't exist")
    }
}
