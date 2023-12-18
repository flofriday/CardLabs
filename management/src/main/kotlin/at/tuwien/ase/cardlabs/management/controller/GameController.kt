package at.tuwien.ase.cardlabs.management.controller

import at.tuwien.ase.cardlabs.management.controller.model.game.Game
import at.tuwien.ase.cardlabs.management.database.model.match.log.LogMessage
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.service.match.GameService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class GameController(
    val gameService: GameService,
) {

    private final val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/match/{gameId}/all")
    fun fetchAllById(
        @AuthenticationPrincipal user: CardLabUser,
        @PathVariable gameId: Long,
    ): ResponseEntity<Game> {
        logger.info("User $user attempts to fetch the game $gameId")
        val game = gameService.fetchById(user, gameId)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(game)
    }

    @GetMapping("/match/{gameId}/log")
    fun fetchLogById(
        @AuthenticationPrincipal user: CardLabUser,
        @PathVariable gameId: Long,
    ): ResponseEntity<List<LogMessage>> {
        logger.info("User ${user.id} attempts to fetch the logs of the game $gameId")
        val logMessages = gameService.fetchLogById(user, gameId)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(logMessages)
    }
}
