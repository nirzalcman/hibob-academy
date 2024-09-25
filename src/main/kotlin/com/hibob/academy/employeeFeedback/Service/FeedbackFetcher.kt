package com.hibob.academy.employeeFeedback.Service

import com.hibob.academy.employeeFeedback.dao.Feedback
import com.hibob.academy.employeeFeedback.dao.FeedbackDao
import com.hibob.academy.employeeFeedback.dao.FeedbackFilter
import com.hibob.academy.employeeFeedback.dao.UserLoggedInDetails
import jakarta.ws.rs.NotFoundException

class FeedbackFetcher(private val feedbackDao: FeedbackDao) {

    fun getFeedbackById(companyId: Long, feedbackId: Long): Feedback =
        feedbackDao.getFeedbackById(companyId, feedbackId) ?: throw NotFoundException("Feedback $feedbackId not found")

    fun getAllFeedbacks(companyId: Long): List<Feedback> =
        feedbackDao.getAllFeedbacks(companyId)

    fun getFeedbacksByFilters(companyId: Long, filter: FeedbackFilter): List<Feedback> =
        feedbackDao.searchFeedbacks(companyId, filter)

    fun getStatus(userLoggedInDetails: UserLoggedInDetails, feedbackId: Long): String =
        feedbackDao.getStatus(userLoggedInDetails, feedbackId)
            ?: throw NotFoundException("Feedback $feedbackId not found")

}