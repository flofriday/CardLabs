package at.tuwien.ase.cardlabs.management.controller

import at.tuwien.ase.cardlabs.management.controller.model.bot.Bot
import at.tuwien.ase.cardlabs.management.controller.model.bot.BotCreate
import at.tuwien.ase.cardlabs.management.controller.model.bot.BotPatch
import at.tuwien.ase.cardlabs.management.controller.model.bot.TestBot
import at.tuwien.ase.cardlabs.management.error.UnauthorizedException
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.service.bot.BotService
import at.tuwien.ase.cardlabs.management.service.game.GameService
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
    val gameService: GameService,
) {
    private final val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/bot/name")
    fun getName(
        @AuthenticationPrincipal user: CardLabUser,
    ): ResponseEntity<String> {
        logger.info("User ${user.id} attempts to generate a bot name")
        val name = botService.generateBotName(user)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(name)
    }

    @GetMapping("/bot/test-bots")
    fun fetchTestBots(
        @AuthenticationPrincipal user: CardLabUser,
    ): ResponseEntity<List<TestBot>> {
        logger.info("User ${user.id} attempts to receive all test bots")
        val testBots = botService.fetchAllTestsBots(user)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(testBots)
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
        logger.info("User ${user.id} attempts to rank the bot $botId")
        botService.rank(user, botId)
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
        val bot = botService.fetch(user, botId)

        if (bot.ownerId != user.id) {
            throw UnauthorizedException("You are not authorized to view the bot $botId")
        }

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(bot)
    }

    @GetMapping("/bot/{botId}/name")
    fun getBotName(
        @AuthenticationPrincipal user: CardLabUser,
        @PathVariable botId: Long,
    ): ResponseEntity<String> {
        logger.info("User ${user.id} fetches the bot name for bot with id $botId")
        if (botId < 0) {
            val result =  botService.fetchTestBotById(botId)
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(result.name)
        }
        val result = botService.fetch(user, botId)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(result.name)
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

    @PostMapping("/bot/{botId}/test-match/{testBotId}")
    fun testMatch(
        @AuthenticationPrincipal user: CardLabUser,
        @PathVariable botId: Long,
        @PathVariable testBotId: Long,
        @RequestParam(required = false, defaultValue = "false") useCurrentCode: Boolean,
    ): ResponseEntity<Long> {
        logger.info("User ${user.id} attempts to create a test match for the user bot $botId against the test bot $testBotId")
        val gameId = gameService.createTestMatch(user, botId, testBotId, useCurrentCode)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(gameId)
    }
}
