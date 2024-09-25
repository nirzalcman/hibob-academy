package com.hibob.academy.employeeFeedback.Service

import com.hibob.academy.employeeFeedback.dao.CreationFeedback
import com.hibob.academy.employeeFeedback.dao.FeedbackDao
import com.hibob.academy.employeeFeedback.dao.UserLoggedInDetails
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import kotlin.random.Random

class FeedbackCreatorTest {
    private val feedbackDaoMock = mock<FeedbackDao>()
    private val feedbackCreator = FeedbackCreator(feedbackDaoMock)
    private val companyId = Random.nextLong()
    private val employeeId = Random.nextLong()

    @Test
    fun `test createFeedBack returns feedback id`() {
        val userLoggedInDetails = UserLoggedInDetails(employeeId, companyId)
        val creationFeedback = CreationFeedback(content = "Test Feedback", isAnonymous = false)
        val expectedFeedbackId = 1L

        whenever(feedbackDaoMock.createFeedback(userLoggedInDetails, creationFeedback)).thenReturn(expectedFeedbackId)

        val result = feedbackCreator.createFeedBack(userLoggedInDetails, creationFeedback)

        assertEquals(expectedFeedbackId, result)
        verify(feedbackDaoMock).createFeedback(userLoggedInDetails, creationFeedback)
    }

    @Test
    fun `test createFeedBack throws exception for invalid feedback content length`() {
        val userLoggedInDetails = UserLoggedInDetails(employeeId, companyId)
        val shortFeedback = CreationFeedback(content = "Short", isAnonymous = false)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            feedbackCreator.createFeedBack(userLoggedInDetails, shortFeedback)
        }
        assertEquals("Feedback content length must be between 10 and 2000 characters.", exception.message)
    }

}