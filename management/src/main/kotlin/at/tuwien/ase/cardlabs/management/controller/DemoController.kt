package at.tuwien.ase.cardlabs.management.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DemoController {

    @GetMapping("/")
    fun foo(@AuthenticationPrincipal user: UserDetails): String {
        return "bar ${user.username}, ${user.authorities}"
    }

    @GetMapping("/test/foo")
    fun testFoo(@AuthenticationPrincipal user: UserDetails): String {
        return "test_foo"
    }

    @GetMapping("/test/user")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    fun testTest(@AuthenticationPrincipal user: UserDetails): String {
        return "test_user"
    }

    @GetMapping("/test/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    fun testAdmin(@AuthenticationPrincipal user: UserDetails): String {
        return "test_admin"
    }
}
