package org.univesp.natalagapebackend.controllers

import JwtTokenProvider
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.univesp.natalagapebackend.dto.LoginRequest
import org.univesp.natalagapebackend.dto.LoginResponse


@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )
        val username = authentication.name
        val authorities = authentication.authorities.joinToString(",") { it.authority }

        val token = jwtTokenProvider.generateToken(username, authorities)
        return ResponseEntity.ok(LoginResponse(token))
    }
}