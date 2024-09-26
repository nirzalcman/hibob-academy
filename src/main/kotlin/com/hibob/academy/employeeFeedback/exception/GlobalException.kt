package com.hibob.academy.employeeFeedback.exception

import jakarta.ws.rs.ForbiddenException
import jakarta.ws.rs.NotFoundException
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import jakarta.ws.rs.core.Response

@Provider
class GlobalExceptionMapper : ExceptionMapper<Exception> {
    override fun toResponse(exception: Exception): Response {
        val status = when (exception) {
            is IllegalArgumentException -> Response.Status.BAD_REQUEST
            is NotFoundException -> Response.Status.NOT_FOUND
            is ForbiddenException -> Response.Status.FORBIDDEN
            else -> Response.Status.INTERNAL_SERVER_ERROR
        }
        return Response.status(status).entity(exception.message).build()
    }
}