package com.hibob.academy.employeeFeedback.dao

import org.junit.jupiter.api.Assertions.*
import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.sql.Date
import java.time.LocalDate
import kotlin.random.Random

@BobDbTest
class FeedbackDaoTest @Autowired constructor(private val sql: DSLContext) {
    private val feedbackTable = FeedbackTable.instance
    private val feedbackDao = FeedbackDao(sql)
    private val companyId = Random.nextLong()
    private val employeeId = Random.nextLong()
    private val userLoggedInDetails = UserLoggedInDetails(employeeId, companyId, Role.EMPLOYEE)
    private val date = Date.valueOf(LocalDate.now())

    private val feedback1 = CreationFeedback(content = "Test Feedback", isAnonymous = false)



    private val feedback2 = CreationFeedback(content = "Feedback 2",isAnonymous = true)


    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(feedbackTable).where(feedbackTable.companyId.eq(companyId)).execute()
    }


    @Test
    fun `create not anonymous feedback successfully`() {

        val feedbackId = feedbackDao.createFeedback(feedback1, userLoggedInDetails)
        val actualFeedback = feedbackDao.getFeedbackById(feedbackId, userLoggedInDetails)
        val expectedFeedback = Feedback(feedbackId, userLoggedInDetails.employeeId, feedback1.content, feedback1.isAnonymous, date, Status.UNREVIEWED, null)

        assertEquals(expectedFeedback, actualFeedback)
    }

    @Test
    fun `create anonymous feedback successfully`() {

        val feedbackId = feedbackDao.createFeedback(feedback2, userLoggedInDetails)
        val actualFeedback = feedbackDao.getFeedbackById(feedbackId, userLoggedInDetails)
        val expectedFeedback = Feedback(feedbackId, null, feedback2.content, feedback2.isAnonymous, date, Status.UNREVIEWED, null)

        assertEquals(expectedFeedback, actualFeedback)
    }


    @Test
    fun `get all feedbacks for company`() {


        val feedbackId1 = feedbackDao.createFeedback(feedback1, userLoggedInDetails)
        val feedbackId2 = feedbackDao.createFeedback(feedback2, userLoggedInDetails)

        val expectedFeedback1 = Feedback(feedbackId1, userLoggedInDetails.employeeId, feedback1.content, feedback1.isAnonymous, date, Status.UNREVIEWED, null)
        val expectedFeedback2 = Feedback(feedbackId2, null, feedback2.content, feedback2.isAnonymous, date, Status.UNREVIEWED, null)
        val actualFeedbacks = feedbackDao.getAllFeedbacks(userLoggedInDetails)

        assertEquals(2, actualFeedbacks.size)
        assertEquals(listOf(expectedFeedback1, expectedFeedback2), actualFeedbacks)


    }

    @Test
    fun `get feedback by id`() {

        val feedbackId = feedbackDao.createFeedback(feedback1, userLoggedInDetails)
        val actualFeedback = feedbackDao.getFeedbackById(feedbackId, userLoggedInDetails)
        val expectedFeedback = Feedback(feedbackId, userLoggedInDetails.employeeId, feedback1.content, feedback1.isAnonymous, date, Status.UNREVIEWED, null)

        assertEquals(expectedFeedback, actualFeedback)
    }







}