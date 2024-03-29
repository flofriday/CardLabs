package at.tuwien.ase.cardlabs.management.controller

import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.security.authentication.AccessTokenAuthenticationResponse
import at.tuwien.ase.cardlabs.management.security.authentication.RefreshTokenRequest
import at.tuwien.ase.cardlabs.management.security.jwt.JwtTokenService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenService: JwtTokenService,
) {
    private final val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping("/authentication/refresh")
    fun refreshToken(
        @RequestBody refreshTokenRequest: RefreshTokenRequest,
    ): ResponseEntity<AccessTokenAuthenticationResponse> {
        logger.info("User attempts to generate a new access token")
        val accessToken = jwtTokenService.generateAccessToken(refreshTokenRequest.refreshToken)
        return ResponseEntity.status(HttpStatus.OK)
            .body(AccessTokenAuthenticationResponse(accessToken))
    }

    @GetMapping("/authentication")
    fun verify(
        @AuthenticationPrincipal user: CardLabUser,
    ): ResponseEntity<Unit> {
        logger.info("User ${user.id} verifies that the JWT token is still valid")
        return ResponseEntity
            .status(HttpStatus.OK)
            .build()
    }
}
