package com.hibob.academy.employeeFeedback.Service

import com.hibob.academy.employeeFeedback.dao.CreationFeedback
import com.hibob.academy.employeeFeedback.dao.FeedbackResponseDao
import com.hibob.academy.employeeFeedback.dao.ResponseCreationRequest
import com.hibob.academy.employeeFeedback.dao.UserLoggedInDetails
import org.springframework.stereotype.Service


@Service
class FeedbackResponseCreator(private val feedbackResponseDao: FeedbackResponseDao) {

    fun createResponse(
        userLoggedInDetails: UserLoggedInDetails,
        responseCreationRequest: ResponseCreationRequest
    ): Long {
        validateFeedbackResponse(responseCreationRequest.content)
        return feedbackResponseDao.createResponse(userLoggedInDetails, responseCreationRequest)
    }

    private fun validateFeedbackResponse(content: String) {
        require(content.length in 10..2000) { "Response content length must be between 10 and 2000 characters." }
    }
}