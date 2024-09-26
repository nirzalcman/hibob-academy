package com.hibob.academy.employeeFeedback.Service

import com.hibob.academy.employeeFeedback.dao.FeedbackResponseDao
import com.hibob.academy.employeeFeedback.dao.ResponseCreationRequest
import org.junit.jupiter.api.Assertions.*
import com.hibob.academy.employeeFeedback.dao.UserLoggedInDetails
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import kotlin.random.Random

class FeedbackResponseCreatorTest {
    private val feedbackResponseDaoMock = mock<FeedbackResponseDao>()
    private val feedbackResponseCreator = FeedbackResponseCreator(feedbackResponseDaoMock)
    private val feedbackId = Random.nextLong()
    private val companyId = Random.nextLong()
    private val employeeId = Random.nextLong()

    @Test
    fun `test createResponse returns response id`() {
        val userLoggedInDetails = UserLoggedInDetails(employeeId, companyId)
        val responseCreationRequest = ResponseCreationRequest(feedbackId, content = "Test Response")
        val expectedResponseId = 1L

        whenever(feedbackResponseDaoMock.createResponse(userLoggedInDetails, responseCreationRequest)).thenReturn(
            expectedResponseId
        )

        val result = feedbackResponseCreator.createResponse(userLoggedInDetails, responseCreationRequest)

        assertEquals(expectedResponseId, result)
        verify(feedbackResponseDaoMock).createResponse(userLoggedInDetails, responseCreationRequest)
    }

    @Test
    fun `test createResponse throws exception for invalid response content length`() {
        val userLoggedInDetails = UserLoggedInDetails(employeeId, companyId)
        val shortResponse = ResponseCreationRequest(feedbackId, content = "Short")

        val exception = assertThrows(IllegalArgumentException::class.java) {
            feedbackResponseCreator.createResponse(userLoggedInDetails, shortResponse)
        }
        assertEquals("Response content length must be between 10 and 2000 characters.", exception.message)
    }
}