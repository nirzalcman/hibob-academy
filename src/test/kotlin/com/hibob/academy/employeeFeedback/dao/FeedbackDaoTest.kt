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
    private val employeeTable = EmployeeTable.instance
    private val employeeDao = EmployeeDao(sql)

    private val companyId = Random.nextLong()
    private val employeeId = Random.nextLong()
    private val userLoggedInDetails = UserLoggedInDetails(employeeId, companyId)
    private val date = Date.valueOf(LocalDate.now())

    private val feedback1 = CreationFeedback(content = "Test Feedback", isAnonymous = false)
    private val feedback2 = CreationFeedback(content = "Feedback 2", isAnonymous = true)


    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(feedbackTable).where(feedbackTable.companyId.eq(companyId)).execute()
        sql.deleteFrom(employeeTable).where(employeeTable.companyId.eq(companyId)).execute()

    }


    @Test
    fun `create not anonymous feedback successfully`() {

        val feedbackId = feedbackDao.createFeedback(userLoggedInDetails,feedback1)
        val actualFeedback = feedbackDao.getFeedbackById(companyId,feedbackId)
        val expectedFeedback = Feedback(
            feedbackId,
            userLoggedInDetails.employeeId,
            feedback1.content,
            feedback1.isAnonymous,
            date,
            Status.UNREVIEWED,
            null
        )

        assertEquals(expectedFeedback, actualFeedback)
    }

    @Test
    fun `create anonymous feedback successfully`() {

        val feedbackId = feedbackDao.createFeedback(userLoggedInDetails,feedback2)
        val actualFeedback = feedbackDao.getFeedbackById(companyId,feedbackId)
        val expectedFeedback =
            Feedback(feedbackId, null, feedback2.content, feedback2.isAnonymous, date, Status.UNREVIEWED, null)

        assertEquals(expectedFeedback, actualFeedback)
    }


    @Test
    fun `get all feedbacks for company`() {


        val feedbackId1 = feedbackDao.createFeedback(userLoggedInDetails,feedback1)
        val feedbackId2 = feedbackDao.createFeedback(userLoggedInDetails,feedback2)

        val expectedFeedback1 = Feedback(
            feedbackId1,
            userLoggedInDetails.employeeId,
            feedback1.content,
            feedback1.isAnonymous,
            date,
            Status.UNREVIEWED,
            null
        )
        val expectedFeedback2 =
            Feedback(feedbackId2, null, feedback2.content, feedback2.isAnonymous, date, Status.UNREVIEWED, null)
        val actualFeedbacks = feedbackDao.getAllFeedbacks(companyId)

        assertEquals(2, actualFeedbacks.size)
        assertEquals(listOf(expectedFeedback1, expectedFeedback2), actualFeedbacks)


    }

    @Test
    fun `get feedback by id`() {

        val feedbackId = feedbackDao.createFeedback(userLoggedInDetails,feedback1)
        val actualFeedback = feedbackDao.getFeedbackById(companyId,feedbackId)
        val expectedFeedback = Feedback(
            feedbackId,
            userLoggedInDetails.employeeId,
            feedback1.content,
            feedback1.isAnonymous,
            date,
            Status.UNREVIEWED,
            null
        )

        assertEquals(expectedFeedback, actualFeedback)
    }

    @Test
    fun `search feedbacks by is anonymous`() {

        val feedBackId1 = feedbackDao.createFeedback(userLoggedInDetails,feedback1)

        val anonymousFilter = FeedbackFilter(isAnonymous = false)
        val actualAnonymousFeedbacks = feedbackDao.searchFeedbacks(companyId,anonymousFilter)
        val expectedFeedbacks = listOf(
            Feedback(
                feedBackId1,
                userLoggedInDetails.employeeId,
                feedback1.content,
                feedback1.isAnonymous,
                date,
                Status.UNREVIEWED,
                null
            )
        )

        assertEquals(expectedFeedbacks, actualAnonymousFeedbacks)

    }

    @Test
    fun `search feedbacks by after date and is anonymous`() {

        val feedbackId1 = feedbackDao.createFeedback(userLoggedInDetails,feedback1)
        val feedbackId2 = feedbackDao.createFeedback(userLoggedInDetails,feedback2)

        val dateForFilter = LocalDate.now().minusDays(3).toString()
        val filter = FeedbackFilter(afterDate = dateForFilter, isAnonymous = true)

        val actualFeedbacks = feedbackDao.searchFeedbacks(companyId,filter)
        val expectedFeedbacks =
            listOf(Feedback(feedbackId2, null, feedback2.content, feedback2.isAnonymous, date, Status.UNREVIEWED, null))

        assertEquals(expectedFeedbacks, actualFeedbacks)
    }

    @Test
    fun `search feedbacks by before date - return empty`() {

        val feedbackId1 = feedbackDao.createFeedback(userLoggedInDetails,feedback1)
        val feedbackId2 = feedbackDao.createFeedback(userLoggedInDetails,feedback2)

        val dateForFilter = LocalDate.now().minusDays(3).toString()
        val filter = FeedbackFilter(beforeDate = dateForFilter)
        val actualFeedbacks = feedbackDao.searchFeedbacks(companyId,filter)

        assertEquals(emptyList<Feedback>(), actualFeedbacks)
    }

    @Test
    fun `search feedbacks by before date and is not anonymous `() {

        val feedbackId1 = feedbackDao.createFeedback(userLoggedInDetails,feedback1)
        val feedbackId2 = feedbackDao.createFeedback(userLoggedInDetails,feedback2)

        val dateForFilter = LocalDate.now().plusDays(3).toString()
        val filter = FeedbackFilter(beforeDate = dateForFilter, isAnonymous = false)

        val actualFeedbacks = feedbackDao.searchFeedbacks(companyId,filter)
        val expectedFeedbacks = listOf(
            Feedback(
                feedbackId1,
                userLoggedInDetails.employeeId,
                feedback1.content,
                feedback1.isAnonymous,
                date,
                Status.UNREVIEWED,
                null
            )
        )

        assertEquals(expectedFeedbacks, actualFeedbacks)
    }

    @Test
    fun `search feedbacks by department and after date`() {

        val idEmployeeGenerated1 = employeeDao.createEmployee(
            CreationEmployee("HI", "BOB", Role.EMPLOYEE, Department.Sales),
            userLoggedInDetails
        )
        val idEmployeeGenerated2 = employeeDao.createEmployee(
            CreationEmployee("BY", "BOB", Role.EMPLOYEE, Department.Finance),
            userLoggedInDetails
        )
        val feedbackId1 =
            feedbackDao.createFeedback( userLoggedInDetails.copy(employeeId = idEmployeeGenerated1),feedback1)
        val feedbackId2 =
            feedbackDao.createFeedback( userLoggedInDetails.copy(employeeId = idEmployeeGenerated2),feedback2)

        val dateForFilter = LocalDate.now().minusDays(3).toString()
        val filter = FeedbackFilter(department = Department.Sales.toString(), afterDate = dateForFilter)

        val actualFeedbacks = feedbackDao.searchFeedbacks(companyId,filter)
        val expectedFeedbacks = listOf(
            Feedback(
                feedbackId1,
                idEmployeeGenerated1,
                feedback1.content,
                feedback1.isAnonymous,
                date,
                Status.UNREVIEWED,
                null
            )
        )

        assertEquals(expectedFeedbacks, actualFeedbacks)
    }

    @Test
    fun `search with out filters return all feedbacks `() {

        val feedBackId1 = feedbackDao.createFeedback(userLoggedInDetails,feedback1)

        val filter = FeedbackFilter()
        val actualFeedbacks = feedbackDao.searchFeedbacks(companyId,filter)
        val expectedFeedbacks = listOf(
            Feedback(
                feedBackId1,
                userLoggedInDetails.employeeId,
                feedback1.content,
                feedback1.isAnonymous,
                date,
                Status.UNREVIEWED,
                null
            )
        )
        assertEquals(expectedFeedbacks, actualFeedbacks)

    }


    @Test
    fun `get status should return correct status for given feedback`() {
        val feedbackId = feedbackDao.createFeedback(userLoggedInDetails,feedback1)
        val status = feedbackDao.getStatus( userLoggedInDetails,feedbackId)

        assertEquals(Status.UNREVIEWED.toString(), status)
    }


    @Test
    fun `get status should return null for non-existing feedback`() {
        val nonExistingFeedbackId = Random.nextLong()
        val status = feedbackDao.getStatus( userLoggedInDetails,nonExistingFeedbackId)
        assertNull(status)
    }

    @Test
    fun `update status should update the status of the feedback`() {

        val feedbackId = feedbackDao.createFeedback(userLoggedInDetails,feedback1)
        feedbackDao.updateStatus(Status.REVIEWED, feedbackId, companyId)

        val actualStatus = feedbackDao.getStatus(userLoggedInDetails,feedbackId)
        assertEquals(Status.REVIEWED.toString(), actualStatus)
    }

    @Test
    fun `update status should not update status for non-existing feedback`() {
        val nonExistingFeedbackId = Random.nextLong()
        assertEquals(0, feedbackDao.updateStatus(Status.REVIEWED, nonExistingFeedbackId, companyId))
    }


}