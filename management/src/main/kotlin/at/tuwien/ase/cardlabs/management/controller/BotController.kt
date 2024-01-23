package at.tuwien.ase.cardlabs.management.controller

import at.tuwien.ase.cardlabs.management.controller.model.bot.Bot
import at.tuwien.ase.cardlabs.management.controller.model.bot.BotCreate
import at.tuwien.ase.cardlabs.management.controller.model.bot.BotPatch
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.service.bot.BotService
import at.tuwien.ase.cardlabs.management.util.Region
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class BotController(
    val botService: BotService,
) {

    private final val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/bot/name")
    fun getName(@AuthenticationPrincipal user: CardLabUser): ResponseEntity<String> {
        logger.info("User ${user.id} attempts to generate a bot name")
        val name = botService.generateBotName(user)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(name)
    }

    @PostMapping("/bot")
    fun create(
        @AuthenticationPrincipal user: CardLabUser,
        @RequestBody botCreate: BotCreate,
    ): ResponseEntity<Bot> {
        logger.info("User ${user.id} attempts to create a bot with the name ${botCreate.name}")
        val result = botService.create(user, botCreate)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result)
    }

    @PatchMapping("/bot/{botId}")
    fun patch(
        @AuthenticationPrincipal user: CardLabUser,
        @PathVariable botId: Long,
        @RequestBody botPatch: BotPatch,
    ): ResponseEntity<Bot> {
        logger.info("User ${user.id} attempts to patch the bot $botId")
        val result = botService.patch(user, botId, botPatch)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(result)
    }

    @PostMapping("/bot/{botId}/rank")
    fun rank(
        @AuthenticationPrincipal user: CardLabUser,
        @PathVariable botId: Long,
    ): ResponseEntity<Unit> {
        logger.info("User ${user.id} attempts to rank the bot and create bot version $botId")
        botService.createCodeVersion(user, botId)
        return ResponseEntity
            .status(HttpStatus.OK)
            .build()
    }

    @GetMapping("/bot/{botId}/rank")
    fun rankPosition(
        @AuthenticationPrincipal user: CardLabUser,
        @PathVariable botId: Long,
        @RequestParam(required = true) region: Region,
    ): ResponseEntity<Long> {
        logger.info("User ${user.id} attempts to fetch bot rank of $botId")
        val rank = botService.fetchRankPosition(user, botId, region)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(rank)
    }

    @GetMapping("/bot/{botId}")
    fun fetch(
        @AuthenticationPrincipal user: CardLabUser,
        @PathVariable botId: Long,
    ): ResponseEntity<Bot> {
        logger.info("User ${user.id} attempts to fetch the bot $botId")
        val result = botService.fetch(user, botId)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(result)
    }

    @GetMapping("/bot")
    fun fetchAll(
        @AuthenticationPrincipal user: CardLabUser,
        @RequestParam(required = false, defaultValue = "0") pageNumber: Int,
        @RequestParam(required = false, defaultValue = "10") pageSize: Int,
    ): ResponseEntity<Page<Bot>> {
        logger.info("User ${user.id} attempts to fetch all its bots (pageNumber=$pageNumber, pageSize=$pageSize)")
        val pageable = PageRequest.of(pageNumber, pageSize)
        val result = botService.fetchAll(user, pageable)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(result)
    }

    @DeleteMapping("/bot/{botId}")
    fun delete(
        @AuthenticationPrincipal user: CardLabUser,
        @PathVariable botId: Long,
    ): ResponseEntity<Unit> {
        logger.info("User ${user.id} attempts to delete the bot $botId")
        botService.delete(user, botId)
        return ResponseEntity
            .status(HttpStatus.OK)
            .build()
    }
}
