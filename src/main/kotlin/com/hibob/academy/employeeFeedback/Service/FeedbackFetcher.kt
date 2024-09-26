package com.hibob.academy.employeeFeedback.Service

import com.hibob.academy.employeeFeedback.dao.*
import jakarta.ws.rs.NotFoundException
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service


@Service
class FeedbackFetcher(private val feedbackDao: FeedbackDao) {

    fun getFeedbackById(companyId: Long, feedbackId: Long): Feedback =
        feedbackDao.getFeedbackById(companyId, feedbackId) ?: throw NotFoundException("Feedback $feedbackId not found")

    fun getAllFeedbacks(companyId: Long): List<Feedback> =
        feedbackDao.getAllFeedbacks(companyId)

    fun getFeedbacksByFilters(companyId: Long, filter: FeedbackFilter): List<Feedback> =
        feedbackDao.searchFeedbacks(companyId, filter)

    fun getStatus(userLoggedInDetails: UserLoggedInDetails, feedbackId: Long): Status =
        feedbackDao.getStatus(userLoggedInDetails, feedbackId)

}