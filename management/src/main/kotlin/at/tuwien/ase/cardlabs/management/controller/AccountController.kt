package at.tuwien.ase.cardlabs.management.controller

import at.tuwien.ase.cardlabs.management.controller.model.Account
import at.tuwien.ase.cardlabs.management.controller.model.AccountUpdate
import at.tuwien.ase.cardlabs.management.error.AccountExistsException
import at.tuwien.ase.cardlabs.management.error.LocationNotFoundException
import at.tuwien.ase.cardlabs.management.error.UnauthorizedException
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.service.AccountService
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
class AccountController(val accountService: AccountService) {

    @PostMapping("/account")
    fun create(@RequestBody account: Account): ResponseEntity<Account> {
        return try {
            val result = accountService.create(account)
            ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result)
        } catch (exception: LocationNotFoundException) {
            ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .build()
        } catch (exception: AccountExistsException) {
            ResponseEntity
                .status(HttpStatus.CONFLICT)
                .build()
        } catch (exception: IllegalArgumentException) {
            ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .build()
        }
    }

    @DeleteMapping("/account/{id}")
    fun delete(
        @AuthenticationPrincipal user: CardLabUser,
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
        return try {
            accountService.delete(user, id)
            ResponseEntity
                .status(HttpStatus.OK)
                .build()
        } catch (exception: UnauthorizedException) {
            ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build()
        } catch (exception: IllegalArgumentException) {
            ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .build()
        }
    }

    @GetMapping("/account")
    fun info(
        @AuthenticationPrincipal user: CardLabUser,
    ): ResponseEntity<Account> {
        return try {
            val result = accountService.getUser(user.username)
            ResponseEntity
                .status(HttpStatus.OK)
                .body(result)
        } catch (exception: UnauthorizedException) {
            ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build()
        } catch (exception: IllegalArgumentException) {
            ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .build()
        }
    }

    @PatchMapping("/account")
    fun update(
        @AuthenticationPrincipal user: CardLabUser,
        @RequestBody accountUpdate: AccountUpdate,
    ): ResponseEntity<Unit> {
        return try {
            accountService.update(user, accountUpdate)
            ResponseEntity
                .status(HttpStatus.OK)
                .build()
        } catch (exception: UnauthorizedException) {
            ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build()
        } catch (exception: IllegalArgumentException) {
            ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .build()
        } catch (exception: LocationNotFoundException) {
            ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .build()
        }
    }
}
