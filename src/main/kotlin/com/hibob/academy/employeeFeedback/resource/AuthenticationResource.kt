package com.hibob.academy.employeeFeedback.resource


import com.hibob.academy.employeeFeedback.Service.SessionJwtService
import com.hibob.academy.employeeFeedback.dao.UserLoggedInDetails

import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.NewCookie

@Controller
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/api")

class AuthenticationResource(private val sessionJwtService: SessionJwtService) {

    @POST
    @Path("/user_login")
    fun loginUser(@RequestBody userLoggedInDetails: UserLoggedInDetails): Response {
        val token = sessionJwtService.createJwtToken(userLoggedInDetails)
        val cookie = NewCookie.Builder("jwt")
            .value(token)
            .path("/api/")
            .build()

        return Response.ok().cookie(cookie).build()
    }

}