package org.univesp.natalagapebackend.dto
import jakarta.validation.constraints.NotBlank
data class LoginRequest(
    @field:NotBlank(message = "O nome de usuário é obrigatório")
    val username: String,
    @field:NotBlank(message = "A senha é obrigatória")
    val password: String
)

data class LoginResponse(
    val token: String
)