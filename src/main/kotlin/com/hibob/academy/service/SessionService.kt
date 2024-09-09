package com.hibob.academy.service


import com.hibob.academy.resource.UserDetails
import io.jsonwebtoken.Claims
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*


@Component
class SessionService {
    private val secretKey = "121212122121212121212121221321323321313123232132312"

    fun createJwtToken(userDetails: UserDetails): String {
        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .claim("email", userDetails.email)
            .claim("username", userDetails.username)
            .claim("isAdmin", userDetails.isAdmin)
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.UTC)))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    fun verify(token: String?): Claims? {
        return token?.let {
            try {
                Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(it)
                    .body
            } catch (e: Exception) {
                null
            }
        }

    }
}






