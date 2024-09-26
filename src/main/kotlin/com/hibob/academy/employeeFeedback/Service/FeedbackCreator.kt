package com.hibob.academy.employeeFeedback.Service

import com.hibob.academy.employeeFeedback.dao.CreationFeedback
import com.hibob.academy.employeeFeedback.dao.FeedbackDao
import com.hibob.academy.employeeFeedback.dao.UserLoggedInDetails
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Service
class FeedbackCreator(private val feedbackDao: FeedbackDao) {
    fun createFeedBack(userLoggedInDetails: UserLoggedInDetails, feedback: CreationFeedback): Long {
        validateFeedback(feedback)
        return feedbackDao.createFeedback(userLoggedInDetails, feedback)
    }

    private fun validateFeedback(feedback: CreationFeedback) {
        require(feedback.content.length in 10..2000) { "Feedback content length must be between 10 and 2000 characters." }
    }
}