package com.hibob.academy.employeeFeedback.Service

import com.hibob.academy.employeeFeedback.dao.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import java.sql.Date
import java.time.LocalDate
import kotlin.random.Random
import jakarta.ws.rs.NotFoundException

class FeedbackFetcherTest {
    private val feedbackDaoMock = mock<FeedbackDao>()
    private val feedbackFetcher = FeedbackFetcher(feedbackDaoMock)
    private val companyId = Random.nextLong()
    private val employeeId = Random.nextLong()
    private val feedbackId = Random.nextLong()
    private val date = Date.valueOf(LocalDate.now())

    private val feedback = Feedback(feedbackId, employeeId, "Test Feedback", false, date, Status.UNREVIEWED, null)


    @Test
    fun `test getFeedbackById returns feedback`() {

        whenever(feedbackDaoMock.getFeedbackById(companyId, feedbackId)).thenReturn(feedback)

        val result = feedbackFetcher.getFeedbackById(companyId, feedbackId)

        assertEquals(feedback, result)
        verify(feedbackDaoMock).getFeedbackById(companyId, feedbackId)
    }

    @Test
    fun `test getFeedbackById throws NotFoundException if feedback not found`() {
        whenever(feedbackDaoMock.getFeedbackById(companyId, feedbackId)).thenReturn(null)

        val exception = assertThrows(NotFoundException::class.java) {
            feedbackFetcher.getFeedbackById(companyId, feedbackId)
        }
        assertEquals("Feedback $feedbackId not found", exception.message)
    }

    @Test
    fun `test getAllFeedbacks returns list of feedbacks`() {
        val feedbacks = listOf(feedback, feedback.copy(id = feedbackId + 1, content = "Test Feedback2"))
        whenever(feedbackDaoMock.getAllFeedbacks(companyId)).thenReturn(feedbacks)

        val result = feedbackFetcher.getAllFeedbacks(companyId)

        assertEquals(feedbacks, result)
        verify(feedbackDaoMock).getAllFeedbacks(companyId)
    }

    @Test
    fun `test getFeedbacksByFilters returns feedbacks for department filter`() {
        val filter = FeedbackFilter(department = "HR")
        val filteredFeedbacks = listOf(feedback)
        whenever(feedbackDaoMock.searchFeedbacks(companyId, filter)).thenReturn(filteredFeedbacks)

        val result = feedbackFetcher.getFeedbacksByFilters(companyId, filter)
        assertEquals(filteredFeedbacks, result)
        verify(feedbackDaoMock).searchFeedbacks(companyId, filter)
    }

    @Test
    fun `test getFeedbacksByFilters returns feedbacks for date range filter`() {
        val filter = FeedbackFilter(afterDate = "2023-01-01", beforeDate = "2024-12-31")
        val filteredFeedbacks = listOf(feedback)
        whenever(feedbackDaoMock.searchFeedbacks(companyId, filter)).thenReturn(filteredFeedbacks)

        val result = feedbackFetcher.getFeedbacksByFilters(companyId, filter)

        assertEquals(filteredFeedbacks, result)
        verify(feedbackDaoMock).searchFeedbacks(companyId, filter)
    }

    @Test
    fun `test getFeedbacksByFilters returns feedbacks for anonymous filter`() {
        val filter = FeedbackFilter(isAnonymous = false)
        val filteredFeedbacks = listOf(feedback)
        whenever(feedbackDaoMock.searchFeedbacks(companyId, filter)).thenReturn(filteredFeedbacks)

        val result = feedbackFetcher.getFeedbacksByFilters(companyId, filter)

        assertEquals(filteredFeedbacks, result)
        verify(feedbackDaoMock).searchFeedbacks(companyId, filter)
    }

    @Test
    fun `test getStatus returns status`() {
        val userLoggedInDetails = UserLoggedInDetails(employeeId, companyId)
        val expectedStatus = Status.UNREVIEWED.toString()
        whenever(feedbackDaoMock.getStatus(userLoggedInDetails, feedbackId)).thenReturn(expectedStatus)

        val result = feedbackFetcher.getStatus(userLoggedInDetails, feedbackId)

        assertEquals(expectedStatus, result)
        verify(feedbackDaoMock).getStatus(userLoggedInDetails, feedbackId)
    }

    @Test
    fun `test getStatus throws NotFoundException if feedback not found`() {
        val userLoggedInDetails = UserLoggedInDetails(employeeId, companyId)
        whenever(feedbackDaoMock.getStatus(userLoggedInDetails, feedbackId)).thenReturn(null)

        val exception = assertThrows(NotFoundException::class.java) {
            feedbackFetcher.getStatus(userLoggedInDetails, feedbackId)
        }
        assertEquals("Feedback $feedbackId not found", exception.message)
    }
}