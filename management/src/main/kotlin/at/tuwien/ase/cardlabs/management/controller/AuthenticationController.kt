package at.tuwien.ase.cardlabs.management.controller

import at.tuwien.ase.cardlabs.management.security.JwtHelper
import at.tuwien.ase.cardlabs.management.security.authentication.JwtAuthenticationResponse
import at.tuwien.ase.cardlabs.management.security.authentication.LoginRequest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController(val authenticationManager: AuthenticationManager) {

    @PostMapping("/authentication/login")
    fun login(@RequestBody loginRequest: LoginRequest): JwtAuthenticationResponse {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(
                loginRequest.username,
                loginRequest.password
            )
        )

        val jwt = JwtHelper.generateToken(authentication)
        return JwtAuthenticationResponse(loginRequest.username, jwt)
    }

}