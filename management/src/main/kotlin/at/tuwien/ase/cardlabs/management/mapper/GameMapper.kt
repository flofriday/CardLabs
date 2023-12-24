package at.tuwien.ase.cardlabs.management.mapper

import at.tuwien.ase.cardlabs.management.controller.model.game.Game
import at.tuwien.ase.cardlabs.management.database.model.game.GameDAO
import org.springframework.stereotype.Component

@Component
class GameMapper {

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
}
