package com.hibob.academy.employeeFeedback.Service

import org.junit.jupiter.api.Assertions.*
import com.hibob.academy.employeeFeedback.dao.FeedbackDao
import com.hibob.academy.employeeFeedback.dao.Status
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import kotlin.random.Random
import jakarta.ws.rs.NotFoundException

class FeedbackUpdaterTest {
    private val feedbackDaoMock = mock<FeedbackDao>()
    private val feedbackUpdater = FeedbackUpdater(feedbackDaoMock)
    private val companyId = Random.nextLong()
    private val feedbackId = Random.nextLong()


    @Test
    fun `test updateStatus returns true when status is updated successfully`() {
        val status = Status.REVIEWED
        whenever(feedbackDaoMock.updateStatus(companyId, status, feedbackId)).thenReturn(1)

        val result = feedbackUpdater.updateStatus(companyId, status, feedbackId)

        assertTrue(result)
        verify(feedbackDaoMock).updateStatus(companyId, status, feedbackId)
    }

    @Test
    fun `test updateStatus throws NotFoundException when feedback is not found`() {
        val status = Status.REVIEWED
        whenever(feedbackDaoMock.updateStatus(companyId, status, feedbackId)).thenReturn(0)

        val exception = assertThrows(NotFoundException::class.java) {
            feedbackUpdater.updateStatus(companyId, status, feedbackId)
        }
        assertEquals("Feedback $feedbackId not found", exception.message)
        verify(feedbackDaoMock).updateStatus(companyId, status, feedbackId)
    }


}