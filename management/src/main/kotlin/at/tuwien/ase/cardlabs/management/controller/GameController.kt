package at.tuwien.ase.cardlabs.management.controller

import at.tuwien.ase.cardlabs.management.controller.model.game.Game
import at.tuwien.ase.cardlabs.management.database.model.match.log.LogMessage
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.service.match.GameService
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

    @GetMapping("/match/{id}/all")
    fun fetchAllById(
        @AuthenticationPrincipal user: CardLabUser,
        @PathVariable id: Long,
    ): ResponseEntity<Game> {
        val game = gameService.fetchAllById(user, id)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(game)
    }

    @GetMapping("/match/{id}/log")
    fun fetchLogById(
        @AuthenticationPrincipal user: CardLabUser,
        @PathVariable id: Long,
    ): ResponseEntity<List<LogMessage>> {
        val logMessages = gameService.fetchLogById(user, id)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(logMessages)
    }
}
