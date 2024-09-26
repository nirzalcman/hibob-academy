package com.hibob.academy.employeeFeedback.exception

import jakarta.ws.rs.ForbiddenException
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider

@Provider
class ForbiddenExceptionMapper : ExceptionMapper<ForbiddenException> {
    override fun toResponse(exception: ForbiddenException): Response {
        return Response.status(Response.Status.UNAUTHORIZED).entity(exception.message).build()
    }
}
