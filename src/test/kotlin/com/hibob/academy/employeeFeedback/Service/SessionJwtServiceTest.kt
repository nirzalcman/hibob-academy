package com.hibob.academy.employeeFeedback.Service

import com.hibob.academy.employeeFeedback.dao.EmployeeDao
import com.hibob.academy.employeeFeedback.dao.Role
import com.hibob.academy.employeeFeedback.dao.UserLoggedInDetails
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import kotlin.random.Random
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class SessionJwtServiceTest {
    private val employeeDaoMock = mock<EmployeeDao>()
    private val sessionJwtService = SessionJwtService(employeeDaoMock)
    private val secretKey = "1212121221212121212121212213213233213131232321"

    private val companyId = Random.nextLong()
    private val employeeId = Random.nextLong()
    private val userLoggedInDetails = UserLoggedInDetails(employeeId, companyId)

    @Test
    fun `test createJwtToken returns a valid token`() {
        val token = sessionJwtService.createJwtToken(userLoggedInDetails)
        val claims = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body

        assertEquals(employeeId, claims["employeeId"])
        assertEquals(companyId, claims["companyId"])
    }

    @Test
    fun `test verify returns claims for a valid token`() {
        val token = Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .claim("employeeId", employeeId)
            .claim("companyId", companyId)
            .setIssuedAt(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)))
            .setExpiration(Date.from(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.UTC)))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()

        val claims = sessionJwtService.verify(token)

        assertEquals(employeeId, claims?.get("employeeId") as Long)
        assertEquals(companyId, claims?.get("companyId") as Long)
    }

    @Test
    fun `test verify returns null for an invalid token`() {
        val invalidToken = "Stamtoken"

        val claims = sessionJwtService.verify(invalidToken)

        assertNull(claims)
    }

    @Test
    fun `test getRoleByLoggedInDetails returns correct role`() {
        val expectedRole = Role.EMPLOYEE
        whenever(employeeDaoMock.getRole(userLoggedInDetails)).thenReturn(expectedRole)

        val role = sessionJwtService.getRoleByLoggedInDetails(employeeId, companyId)

        assertEquals(expectedRole, role)
        verify(employeeDaoMock).getRole(userLoggedInDetails)
    }
}