package org.univesp.natalagapebackend.security

import JwtTokenProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfig() {

    @Bean
    fun jwtTokenProvider(@Value("\${JWT_SECRET_KEY}") secret: String): JwtTokenProvider {
        return JwtTokenProvider(secret)
    }
}