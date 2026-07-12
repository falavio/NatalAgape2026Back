import io.jsonwebtoken.Claims
import org.springframework.beans.factory.annotation.Value
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    @Value("\${JWT_SECRET_KEY}") private val secret: String
) {

    private
    val secretKey: SecretKey = Keys.hmacShaKeyFor(secret.toByteArray())

    fun generateToken(username: String, authorities: String): String {
        val now = Date()
        val expiryDate = Date(now.time + 7200000) // 2 horas
        return Jwts.builder().setSubject(username).setIssuedAt(now).setExpiration(expiryDate)
            .claim("authorities", authorities).signWith(secretKey, SignatureAlgorithm.HS256).compact()
    }


    // Extrai o nome de usuário do token
    fun extractUsername(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }

    // Extrai a data de expiração do token
    fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    // Verifica se o token é válido
    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    // Verifica se o token expirou
    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }


    private fun <T> extractClaim(token: String, claimsResolver: java.util.function.Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }


    // Extrai todas as informações do token
    private fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder().setSigningKey(secret.toByteArray()).build().parseClaimsJws(token).body
    }
}