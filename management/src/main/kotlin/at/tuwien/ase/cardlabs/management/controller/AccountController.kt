package at.tuwien.ase.cardlabs.management.controller

import at.tuwien.ase.cardlabs.management.controller.model.Account
import at.tuwien.ase.cardlabs.management.error.AccountExistsException
import at.tuwien.ase.cardlabs.management.service.AccountService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountController(val accountService: AccountService) {

    @PostMapping("/account/create")
    fun create(@RequestBody account: Account): ResponseEntity<Account> {
        return try {
            val result = accountService.create(account);
            ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result)
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

}