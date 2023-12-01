package at.tuwien.ase.cardlabs.management.controller

import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.security.authentication.JwtAuthenticationResponse
import at.tuwien.ase.cardlabs.management.security.authentication.LoginRequest
import at.tuwien.ase.cardlabs.management.security.jwt.JwtHelper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController(val authenticationManager: AuthenticationManager) {

    @PostMapping("/authentication/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<JwtAuthenticationResponse> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(
                loginRequest.username,
                loginRequest.password,
            ),
        )

        val jwt = JwtHelper.generateToken(authentication)
        return ResponseEntity.status(HttpStatus.OK)
            .body(JwtAuthenticationResponse(loginRequest.username, jwt))
    }

    @GetMapping("/authentication")
    fun verify(@AuthenticationPrincipal user: CardLabUser): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .build()
    }
}
