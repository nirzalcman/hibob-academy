package com.hibob.academy.resource

import com.hibob.academy.service.SessionService
import jakarta.ws.rs.*
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.NewCookie

@Controller
@Path("/api")

class SessionResource(private val sessionService: SessionService) {

    companion object {
        const val COOKIE_NAME = "jwt"
        const val COOKIE_PATH = "/"
        const val COOKIE_MAX_AGE = 24 * 60 * 60 // 1 day in seconds

    }

    @POST
    @Path("/user_session")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    fun generateToken(@RequestBody userDetails: UserDetails): Response {
        val token = sessionService.createJwtToken(userDetails)
        val cookie = NewCookie.Builder(COOKIE_NAME)
            .value(token)
            .path(COOKIE_PATH)
            .maxAge(COOKIE_MAX_AGE)
            .build()

        return Response.ok().cookie(cookie).build()
    }


    @GET
    @Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    fun getTest(): Response {
        return Response.ok().build()
    }

}


data class UserDetails(val email: String, val username: String, val isAdmin: Boolean) {

}