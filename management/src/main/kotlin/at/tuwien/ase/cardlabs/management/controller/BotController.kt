package at.tuwien.ase.cardlabs.management.controller

import at.tuwien.ase.cardlabs.management.controller.model.bot.Bot
import at.tuwien.ase.cardlabs.management.controller.model.bot.BotCreate
import at.tuwien.ase.cardlabs.management.controller.model.bot.BotUpdate
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.service.bot.BotService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class BotController(
    val botService: BotService
) {

    @GetMapping("/bot/name")
    fun getName(@AuthenticationPrincipal user: CardLabUser): ResponseEntity<String> {
        val name = botService.generateBotName()
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(name)
    }

    @PostMapping("/bot")
    fun create(
        @AuthenticationPrincipal user: CardLabUser,
        @RequestBody botCreate: BotCreate
    ): ResponseEntity<Bot> {
        val result = botService.create(user, botCreate)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result)
    }

    @PatchMapping("/bot/{id}")
    fun patch(
        @AuthenticationPrincipal user: CardLabUser,
        @PathVariable id: Long,
        @RequestBody botUpdate: BotUpdate
    ): ResponseEntity<Bot> {
        val result = botService.patch(user, id, botUpdate)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(result)
    }

    @PatchMapping("/bot/{id}/rank")
    fun rank(
        @AuthenticationPrincipal user: CardLabUser,
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        botService.rank(user, id)
        return ResponseEntity
            .status(HttpStatus.OK)
            .build()
    }

    @GetMapping("/bot/{id}")
    fun fetch(
        @AuthenticationPrincipal user: CardLabUser,
        @PathVariable id: Long
    ): ResponseEntity<Bot> {
        val result = botService.fetch(user, id)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(result)
    }

    @DeleteMapping("/bot/{id}")
    fun delete(
        @AuthenticationPrincipal user: CardLabUser,
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        botService.delete(user, id)
        return ResponseEntity
            .status(HttpStatus.OK)
            .build()
    }
}
