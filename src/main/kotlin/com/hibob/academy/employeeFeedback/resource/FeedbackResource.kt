package com.hibob.academy.employeeFeedback.resource

import com.hibob.academy.employeeFeedback.Service.FeedbackCreator
import com.hibob.academy.employeeFeedback.Service.FeedbackFetcher
import com.hibob.academy.employeeFeedback.Service.FeedbackResponseCreator
import com.hibob.academy.employeeFeedback.Service.FeedbackUpdater
import com.hibob.academy.employeeFeedback.annotations.AllowedRoles
import com.hibob.academy.employeeFeedback.dao.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestBody

@Component
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/api/feedback")

class FeedbackResource(
    private val feedbackCreator: FeedbackCreator,
    private val feedbackFetcher: FeedbackFetcher,
    private val feedbackUpdater: FeedbackUpdater,
    private val feedbackResponseCreator: FeedbackResponseCreator,
    private val request: HttpServletRequest
) {

    @POST
    fun createFeedback(@RequestBody creationFeedback: CreationFeedback): Response {
        val userLoggedInDetails = getUserDetails()
        return Response.ok(feedbackCreator.createFeedBack(userLoggedInDetails, creationFeedback)).build()
    }

    @GET
    @AllowedRoles(Role.ADMIN, Role.HR)
    fun getAllFeedbacks(): Response {
        val userLoggedInDetails = getUserDetails()
        return Response.ok(feedbackFetcher.getAllFeedbacks(userLoggedInDetails.companyId)).build()
    }

    @GET
    @Path("/{id}")
    @AllowedRoles(Role.ADMIN, Role.HR)
    fun getFeedbackById(@PathParam("id") feedbackId: Long): Response {
        val userLoggedInDetails = getUserDetails()
        return Response.ok(feedbackFetcher.getFeedbackById(userLoggedInDetails.companyId, feedbackId)).build()
    }

    @POST
    @Path("/filter")
    @AllowedRoles(Role.ADMIN, Role.HR)
    fun getFeedbacksByFilter(@RequestBody feedbackFilter: FeedbackFilter): Response {
        val userLoggedInDetails = getUserDetails()
        return Response.ok(feedbackFetcher.getFeedbacksByFilters(userLoggedInDetails.companyId, feedbackFilter)).build()
    }

    @GET
    @Path("/{id}/status")
    fun getFeedbackStatus(@PathParam("id") feedbackId: Long): Response {
        val userLoggedInDetails = getUserDetails()
        return Response.ok(feedbackFetcher.getStatus(userLoggedInDetails, feedbackId)).build()
    }

    @PUT
    @Path("/{id}/status/{status}")
    @AllowedRoles(Role.HR)
    fun updateStatus(@PathParam("id") feedbackId: Long, @PathParam("status") status: String): Response {
        val userLoggedInDetails = getUserDetails()
        return Response.ok(
            feedbackUpdater.updateStatus(
                userLoggedInDetails.companyId,
                enumValueOf(status),
                feedbackId
            )
        ).build()
    }

    @POST
    @Path("/id/response")
    @AllowedRoles(Role.HR)
    fun createResponse(@RequestBody responseCreationRequest: ResponseCreationRequest): Response {
        val userLoggedInDetails = getUserDetails()
        return Response.ok(feedbackResponseCreator.createResponse(userLoggedInDetails, responseCreationRequest)).build()
    }


    private fun getUserDetails(): UserLoggedInDetails {
        val employeeId = request.getAttribute("employeeId") as Long
        val companyId = request.getAttribute("companyId") as Long
        return UserLoggedInDetails(employeeId, companyId)
    }
}