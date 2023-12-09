package at.tuwien.ase.cardlabs.management.mapper

import at.tuwien.ase.cardlabs.management.controller.model.game.Game
import at.tuwien.ase.cardlabs.management.database.model.match.GameDAO
import org.springframework.stereotype.Component

@Component
class GameMapper {

    fun map(game: Game): GameDAO {
        val gameDAO = GameDAO()
        gameDAO.id = game.id
        gameDAO.startTime = game.startTime
        gameDAO.endTime = game.endTime
        gameDAO.actions = game.actions
        gameDAO.results = game.results
        gameDAO.logMessages = game.logMessages
        return gameDAO
    }

    fun map(gameDAO: GameDAO): Game {
        return Game(
            id = gameDAO.id,
            startTime = gameDAO.startTime,
            endTime = gameDAO.endTime,
            actions = gameDAO.actions,
            results = gameDAO.results,
            logMessages = gameDAO.logMessages
        )
    }
}
