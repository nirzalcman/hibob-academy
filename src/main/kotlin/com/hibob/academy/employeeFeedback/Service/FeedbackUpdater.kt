package com.hibob.academy.employeeFeedback.Service


import com.hibob.academy.employeeFeedback.dao.*
import com.hibob.academy.employeeFeedback.dao.FeedbackDao
import jakarta.ws.rs.NotFoundException
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Service
class FeedbackUpdater(private val feedbackDao: FeedbackDao) {

    fun updateStatus(companyId: Long, status: Status, feedbackId: Long): Boolean =
        if (feedbackDao.updateStatus(
                companyId,
                status,
                feedbackId,
            ) > 0
        ) true else throw NotFoundException("Feedback $feedbackId not found")
}