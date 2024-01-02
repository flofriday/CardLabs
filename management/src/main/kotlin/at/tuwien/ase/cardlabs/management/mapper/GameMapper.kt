package at.tuwien.ase.cardlabs.management.mapper

import at.tuwien.ase.cardlabs.management.controller.model.game.Game
import at.tuwien.ase.cardlabs.management.database.model.game.GameDAO
import at.tuwien.ase.cardlabs.management.matchmaker.MatchResultQueueMessage
import org.springframework.stereotype.Component

@Component
class GameMapper(
    private val roundMapper: RoundMapper,
) {

    fun map(game: Game): GameDAO {
        val gameDAO = GameDAO()
        gameDAO.id = game.id
        gameDAO.startTime = game.startTime
        gameDAO.endTime = game.endTime
        gameDAO.winningBotId = game.winningBotId
        gameDAO.rounds = game.rounds
        gameDAO.gameState = game.gameState
        return gameDAO
    }

    fun map(gameDAO: GameDAO): Game {
        return Game(
            id = gameDAO.id,
            startTime = gameDAO.startTime,
            endTime = gameDAO.endTime,
            winningBotId = gameDAO.winningBotId,
            rounds = gameDAO.rounds,
            gameState = gameDAO.gameState,
        )
    }

    fun map(matchResultQueueMessage: MatchResultQueueMessage): GameDAO {
        val gameDAO = GameDAO()
        gameDAO.id = matchResultQueueMessage.gameId
        gameDAO.startTime = matchResultQueueMessage.startTime
        gameDAO.endTime = matchResultQueueMessage.endTime
        gameDAO.winningBotId = matchResultQueueMessage.winningBotId
        gameDAO.rounds = matchResultQueueMessage.rounds.map(roundMapper::mapRound).toList()
        return gameDAO
    }
}
