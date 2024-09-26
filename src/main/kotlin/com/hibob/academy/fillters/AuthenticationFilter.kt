/*package com.hibob.academy.fillters

import com.hibob.academy.service.SessionService
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.core.Cookie
import org.springframework.stereotype.Component
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.Provider


const val JWT_COOKIE_NAME = "jwt"
const val PATH = "api/user_session"
@Provider
@Component
class AuthenticationFillter(private val sessionService: SessionService) : ContainerRequestFilter {
    override fun filter(requestContext: ContainerRequestContext) {
        if (requestContext.uriInfo.path == PATH) return
        val jwtCookie: Cookie? = requestContext.cookies[JWT_COOKIE_NAME]
        val claims = jwtCookie?.let { sessionService.verify(it.value) }

        if (claims == null) {
            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized: Invalid or missing JWT").build()
            )
        }


    }


}

*/