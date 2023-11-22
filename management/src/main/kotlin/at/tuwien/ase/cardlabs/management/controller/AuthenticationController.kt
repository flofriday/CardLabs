package at.tuwien.ase.cardlabs.management.controller

import at.tuwien.ase.cardlabs.management.security.jwt.JwtHelper
import at.tuwien.ase.cardlabs.management.security.authentication.JwtAuthenticationResponse
import at.tuwien.ase.cardlabs.management.security.authentication.LoginRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController(val authenticationManager: AuthenticationManager) {

    @PostMapping("/authentication/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<JwtAuthenticationResponse> {
        return try {
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(
                    loginRequest.username,
                    loginRequest.password
                )
            )

            val jwt = JwtHelper.generateToken(authentication)
            ResponseEntity.status(HttpStatus.OK)
                .body(JwtAuthenticationResponse(loginRequest.username, jwt))
        } catch (exception: BadCredentialsException) {
            ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build()
        }
    }
}
