package at.tuwien.ase.cardlabs.management.controller

import at.tuwien.ase.cardlabs.management.controller.model.account.Account
import at.tuwien.ase.cardlabs.management.controller.model.account.AccountUpdate
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.service.AccountService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountController(val accountService: AccountService) {

    private final val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping("/account")
    fun create(@RequestBody account: Account): ResponseEntity<Account> {
        logger.debug("Attempting to create an account with the username ${account.username}")
        val result = accountService.create(account)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result)
    }

    @DeleteMapping("/account")
    fun delete(
        @AuthenticationPrincipal user: CardLabUser,
    ): ResponseEntity<Unit> {
        logger.debug("User ${user.id} attempts to delete its account")
        accountService.delete(user, user.id)
        return ResponseEntity
            .status(HttpStatus.OK)
            .build()
    }

    @GetMapping("/account")
    fun info(
        @AuthenticationPrincipal user: CardLabUser,
    ): ResponseEntity<Account> {
        logger.info("User ${user.id} attempts to fetch its account information")
        val result = accountService.fetchUser(user)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(result)
    }

    @PatchMapping("/account")
    fun update(
        @AuthenticationPrincipal user: CardLabUser,
        @RequestBody accountUpdate: AccountUpdate,
    ): ResponseEntity<Unit> {
        logger.debug("User ${user.id} attempts to update its account")
        accountService.update(user, accountUpdate)
        return ResponseEntity
            .status(HttpStatus.OK)
            .build()
    }
}
