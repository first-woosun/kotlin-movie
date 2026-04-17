package movie.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException::class)
    fun handleJsonError(): ResponseEntity<Unit> = ResponseEntity.badRequest().build()
}
