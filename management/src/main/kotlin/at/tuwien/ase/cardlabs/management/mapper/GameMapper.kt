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
        gameDAO.disqualifiedBotId = game.disqualifiedBotId
        gameDAO.turns = game.turns
        gameDAO.gameState = game.gameState
        return gameDAO
    }

    fun map(gameDAO: GameDAO): Game {
        return Game(
            id = gameDAO.id,
            startTime = gameDAO.startTime,
            endTime = gameDAO.endTime,
            winningBotId = gameDAO.winningBotId,
            disqualifiedBotId = gameDAO.disqualifiedBotId,
            turns = gameDAO.turns,
            gameState = gameDAO.gameState,
        )
    }

    fun map(matchResultQueueMessage: MatchResultQueueMessage): GameDAO {
        val gameDAO = GameDAO()
        gameDAO.id = matchResultQueueMessage.gameId
        gameDAO.startTime = matchResultQueueMessage.startTime
        gameDAO.endTime = matchResultQueueMessage.endTime
        gameDAO.winningBotId = matchResultQueueMessage.winningBotId
        gameDAO.disqualifiedBotId = matchResultQueueMessage.disqualifiedBotId
        gameDAO.turns = matchResultQueueMessage.turns.map(roundMapper::mapRound).toList()
        return gameDAO
    }
}
