package at.tuwien.ase.cardlabs.management.error

import at.tuwien.ase.cardlabs.management.error.account.AccountDoesNotExistException
import at.tuwien.ase.cardlabs.management.error.account.AccountExistsException
import at.tuwien.ase.cardlabs.management.error.account.LocationNotFoundException
import at.tuwien.ase.cardlabs.management.error.authentication.InvalidTokenException
import at.tuwien.ase.cardlabs.management.error.authentication.TokenExpiredException
import at.tuwien.ase.cardlabs.management.error.bot.BotDoesNotExistException
import at.tuwien.ase.cardlabs.management.error.bot.BotStateException
import at.tuwien.ase.cardlabs.management.error.game.GameDoesNotExistException
import io.jsonwebtoken.ExpiredJwtException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    private final val logger = LoggerFactory.getLogger(javaClass)

    // This is used to catch JSON parsing errors when, for example, a required field is missing for a @RequestBody
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<String> {
        logger.warn(ex.toString())
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ex.message)
    }

    // == Account exceptions ==
    @ExceptionHandler(AccountDoesNotExistException::class)
    fun handleAccountDoesNotExistException(ex: AccountDoesNotExistException): ResponseEntity<String> {
        logger.warn(ex.toString())
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ex.message)
    }

    @ExceptionHandler(AccountExistsException::class)
    fun handleAccountExistsException(ex: AccountExistsException): ResponseEntity<String> {
        logger.warn(ex.toString())
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ex.message)
    }

    @ExceptionHandler(LocationNotFoundException::class)
    fun handleLocationNotFoundException(ex: LocationNotFoundException): ResponseEntity<String> {
        logger.warn(ex.toString())
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ex.message)
    }

    // == Bot exceptions ==
    @ExceptionHandler(BotDoesNotExistException::class)
    fun handleBotDoesNotExistException(ex: BotDoesNotExistException): ResponseEntity<String> {
        logger.warn(ex.toString())
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ex.message)
    }

    @ExceptionHandler(BotStateException::class)
    fun handleBotStateException(ex: BotStateException): ResponseEntity<String> {
        logger.warn(ex.toString())
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ex.message)
    }

    // == Game exceptions ==
    @ExceptionHandler(GameDoesNotExistException::class)
    fun handleGameDoesNotExitException(ex: GameDoesNotExistException): ResponseEntity<String> {
        logger.warn(ex.toString())
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ex.message)
    }

    // == Authentication exceptions ==
    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(ex: UnauthorizedException): ResponseEntity<String> {
        logger.warn(ex.toString())
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ex.message)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentials(ex: BadCredentialsException): ResponseEntity<String> {
        logger.warn(ex.toString())
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ex.message)
    }

    @ExceptionHandler(TokenExpiredException::class)
    fun handleTokenExpired(ex: TokenExpiredException): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ex.message)
    }

    @ExceptionHandler(InvalidTokenException::class)
    fun handleInvalidToken(ex: InvalidTokenException): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ex.message)
    }

    @ExceptionHandler(ExpiredJwtException::class)
    fun handleExpiredJwtException(ex: ExpiredJwtException): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .build()
    }

    @ExceptionHandler(UsernameNotFoundException::class)
    fun handleUsernameNotFoundException(ex: UsernameNotFoundException): ResponseEntity<String> {
        logger.warn(ex.toString())
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ex.message)
    }

    // == Generic exceptions ==
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<String> {
        logger.warn(ex.toString())
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ex.message)
    }

    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(ex: ValidationException): ResponseEntity<String> {
        logger.warn(ex.toString())
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ex.message)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: Runtime): ResponseEntity<String> {
        logger.warn(ex.toString())
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .build()
    }
}
