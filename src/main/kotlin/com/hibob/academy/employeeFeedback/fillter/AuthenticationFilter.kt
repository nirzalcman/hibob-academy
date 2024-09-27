package com.hibob.academy.employeeFeedback.fillter

import com.hibob.academy.employeeFeedback.Service.SessionJwtService
import io.jsonwebtoken.Claims
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.core.Cookie
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.Provider
import org.springframework.stereotype.Component

const val JWT_COOKIE_NAME = "jwt"
const val LOGIN_PATH = "api/user_login"
private const val EMPLOYEE_ID = "employeeId"
private const val COMPANY_ID = "companyId"

@Provider
@Component
class AuthenticationFilter(
    private val sessionJwtService: SessionJwtService,
) : ContainerRequestFilter {

    override fun filter(requestContext: ContainerRequestContext) {
        if (requestContext.uriInfo.path == LOGIN_PATH) return

        val jwtCookie: Cookie? = requestContext.cookies[JWT_COOKIE_NAME]
        val claims = jwtCookie?.let { sessionJwtService.verify(it.value) }

        if (claims == null) {
            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized: Invalid or missing JWT").build()
            )
            return
        }

        val employeeId = getValueFromClaims(claims, EMPLOYEE_ID)
        val companyId = getValueFromClaims(claims, COMPANY_ID)

        requestContext.setProperty(EMPLOYEE_ID, employeeId)
        requestContext.setProperty(COMPANY_ID, companyId)
    }

    private fun getValueFromClaims(claims: Claims, key: String): Long {
        return claims.get(key).toString().toLong()
    }

}