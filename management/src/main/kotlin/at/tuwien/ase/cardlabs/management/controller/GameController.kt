package at.tuwien.ase.cardlabs.management.controller

import at.tuwien.ase.cardlabs.management.controller.model.game.Game
import at.tuwien.ase.cardlabs.management.database.model.game.log.LogMessage
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.service.game.GameService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
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
        logger.info("User $user to fetches the game with id $gameId")
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
        logger.info("User ${user.id}  fetches the logs of the game with id $gameId")
        val logMessages = gameService.fetchLogById(user, gameId)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(logMessages)
    }

    @GetMapping("/match/bot/{botId}")
    fun fetchAllGamesWithBot(
        @AuthenticationPrincipal user: CardLabUser,
        @PathVariable botId: Long,
        @RequestParam(required = false, defaultValue = "0") pageNumber: Int,
        @RequestParam(required = false, defaultValue = "10") pageSize: Int,
    ): ResponseEntity<Page<Game>> {
        logger.info("User ${user.id}  fetches all the matches from bot with id $botId (pageNumber=$pageNumber, pageSize=$pageSize)")
        val pageable = PageRequest.of(pageNumber, pageSize)
        val result = gameService.fetchAllGamesWithBot(user, botId, pageable)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(result)
    }
}
