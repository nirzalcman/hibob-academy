package com.hibob.academy.fillters

import com.hibob.academy.service.SessionService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.core.Cookie
import org.springframework.stereotype.Component
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.Provider

@Provider
@Component
class AuthenticationFilter(private val sessionService: SessionService) : ContainerRequestFilter {

    override fun filter(requestContext: ContainerRequestContext) {
        if (requestContext.uriInfo.path == "api/user_session") return
        val jwtCookie: Cookie? = requestContext.cookies["jwt"]
        val claims: Claims? = sessionService.verify(jwtCookie?.value)

        if (claims == null) {
            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized: Invalid or missing JWT").build()
            )
        }


    }


}

