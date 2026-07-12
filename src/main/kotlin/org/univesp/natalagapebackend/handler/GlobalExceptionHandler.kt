package org.univesp.natalagapebackend.handler

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler
    fun handleMaxContributionException(ex: MaxContributionException): ResponseEntity<String> {
        return ResponseEntity
            .status(ex.status)
            .body(ex.message)
    }
}