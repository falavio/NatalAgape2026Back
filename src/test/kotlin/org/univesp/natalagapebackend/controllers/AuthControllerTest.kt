//package org.univesp.natalagapebackend.controllers
//
//import JwtTokenProvider
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.extension.ExtendWith
//import org.mockito.InjectMocks
//import org.mockito.Mock
//import org.mockito.Mockito
//import org.mockito.junit.jupiter.MockitoExtension
//import org.springframework.http.ResponseEntity
//import org.springframework.security.authentication.AuthenticationManager
//import org.springframework.security.authentication.BadCredentialsException
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
//import org.springframework.security.core.authority.SimpleGrantedAuthority
//import org.univesp.natalagapebackend.dto.LoginRequest
//import org.univesp.natalagapebackend.dto.LoginResponse
//
//@ExtendWith(MockitoExtension::class)
//class AuthControllerTest {
//
//    @Mock
//    private lateinit var authenticationManager: AuthenticationManager
//
//    @Mock
//    private lateinit var jwtTokenProvider: JwtTokenProvider
//
//    @InjectMocks
//    private lateinit var authController: AuthController
//
//    @Test
//    fun `deve retornar token ao realizar login`() {
//        // Arrange
//        val username = "user"
//        val password = "password"
//        val token = "jwt-token"
//        val loginRequest = LoginRequest(username, password)
//        val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
//        val authentication = UsernamePasswordAuthenticationToken(username, password)
//
//        Mockito.`when`(authenticationManager.authenticate(authentication)).thenReturn(authentication)
//        Mockito.`when`(jwtTokenProvider.generateToken(authentication.toString())).thenReturn(token)
//
//        // Act
//        val response: ResponseEntity<LoginResponse> = authController.login(loginRequest)
//
//        // Assert
//        assertEquals(200, response.statusCodeValue)
//        assertEquals(token, response.body?.token)
//    }
//
//    @Test
//    fun `deve retornar 401 quando as credenciais forem invalidas`() {
//        // Arrange
//        val username = "invalidUser"
//        val password = "invalidPassword"
//        val loginRequest = LoginRequest(username, password)
//        val authentication = UsernamePasswordAuthenticationToken(username, password)
//
//        Mockito.`when`(authenticationManager.authenticate(authentication))
//            .thenThrow(BadCredentialsException("Credenciais inválidas"))
//
//        // Act
//        val exception = org.junit.jupiter.api.assertThrows<BadCredentialsException> {
//            authController.login(loginRequest)
//        }
//
//        // Assert
//        assertEquals("Credenciais inválidas", exception.message)
//    }
//}