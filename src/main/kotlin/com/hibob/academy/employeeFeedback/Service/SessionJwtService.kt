package com.hibob.academy.employeeFeedback.Service

import com.hibob.academy.employeeFeedback.dao.EmployeeDao
import com.hibob.academy.employeeFeedback.dao.Role
import com.hibob.academy.employeeFeedback.dao.UserLoggedInDetails
import io.jsonwebtoken.Claims
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*


@Component
class SessionJwtService(private val employeeDao: EmployeeDao) {
    private val secretKey = "1212121221212121212121212213213233213131232321"

    fun createJwtToken(userLoggedInDetails: UserLoggedInDetails): String {
        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .claim("employeeId", userLoggedInDetails.employeeId)
            .claim("companyId", userLoggedInDetails.companyId)
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.UTC)))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }


    fun verify(token: String?): Claims? {
        return try {
            Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            null
        }

    }

    fun getRoleByLoggedInDetails(employeeId: Long, companyId: Long): Role {
        val userLoggedInDetails = UserLoggedInDetails(employeeId, companyId)
        return employeeDao.getRole(userLoggedInDetails)

    }
}