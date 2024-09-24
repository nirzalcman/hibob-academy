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
class FeedbackResponseDaoTest @Autowired constructor(private val sql: DSLContext) {
    private val feedbackResponseTable = FeedbackResponseTable.instance
    private val feedbackTable = FeedbackTable.instance

    private val feedbackResponseDao = FeedbackResponseDao(sql)
    private val feedbackDao = FeedbackDao(sql)

    private val companyId = Random.nextLong()
    private val employeeId = Random.nextLong()
    private val userLoggedInDetails = UserLoggedInDetails(employeeId, companyId)
    private val feedbackId = Random.nextLong()
    private val date = Date.valueOf(LocalDate.now())

    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(feedbackResponseTable).where(feedbackResponseTable.companyId.eq(companyId)).execute()
        sql.deleteFrom(feedbackTable).where(feedbackTable.companyId.eq(companyId)).execute()
    }

    @Test
    fun `get response by id when no response with this id `() {
        val responseId = feedbackResponseDao.createResponse(
            userLoggedInDetails,
            ResponseCreationRequest(feedbackId, content = "This is a response")
        )
        val actualResponse = feedbackResponseDao.getResponseById(companyId, responseId + 1)
        assertNull(actualResponse)


    }

    @Test
    fun `create response without conflict and retrieve by id `() {
        val responseCreationRequest = ResponseCreationRequest(feedbackId, content = "This is a response")
        val responseId = feedbackResponseDao.createResponse(userLoggedInDetails, responseCreationRequest)

        val actualResponse = feedbackResponseDao.getResponseById(companyId, responseId)
        val expectedResponse =
            Response(responseId, feedbackId, "This is a response", date, userLoggedInDetails.employeeId)
        assertEquals(expectedResponse, actualResponse)

    }

    @Test
    fun `create response with conflict - update the content of the response `() {
        val responseId1 = feedbackResponseDao.createResponse(
            userLoggedInDetails,
            ResponseCreationRequest(feedbackId, content = "This is a response")
        )
        val responseId2 = feedbackResponseDao.createResponse(
            userLoggedInDetails,
            ResponseCreationRequest(feedbackId, content = "update response")
        )

        val actualResponse = feedbackResponseDao.getResponseById(companyId, responseId1)
        val expectedResponse =
            Response(responseId1, feedbackId, "update response", date, userLoggedInDetails.employeeId)

        assertEquals(responseId1, responseId2)
        assertEquals(expectedResponse, actualResponse)
    }
}
