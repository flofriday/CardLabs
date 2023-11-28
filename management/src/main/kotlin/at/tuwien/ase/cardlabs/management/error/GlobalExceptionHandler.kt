package at.tuwien.ase.cardlabs.management.error

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    // This is used to catch JSON parsing errors when, for example, a required field is missing for a @RequestBody
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ex.message)
    }

    @ExceptionHandler(AccountDoesNotExistException::class)
    fun handleAccountDoesNotExistException(ex: AccountDoesNotExistException): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ex.message)
    }

    @ExceptionHandler(BotDoesNotExistException::class)
    fun handleBotDoesNotExistException(ex: BotDoesNotExistException): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ex.message)
    }

    // == Authentication exceptions ==
    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(ex: UnauthorizedException): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ex.message)
    }

    // == Generic exceptions ==
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ex.message)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: Runtime): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .build()
    }
}
