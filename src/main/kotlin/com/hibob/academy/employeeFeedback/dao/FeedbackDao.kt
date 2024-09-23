package com.hibob.academy.employeeFeedback.dao

import org.jooq.RecordMapper
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.stereotype.Component
import java.sql.Date
import java.time.LocalDate

@Component
class FeedbackDao(private val sql: DSLContext) {
    private val feedBackTable = FeedbackTable.instance

    private val feedBackMapper = RecordMapper<Record, Feedback> { record ->
        Feedback(
            record[feedBackTable.id],
            record[feedBackTable.employeeId],
            record[feedBackTable.content],
            record[feedBackTable.isAnonymous],
            record[feedBackTable.timeSubmitted],
            enumValueOf<Status>(record[feedBackTable.status].toString()),
            record[feedBackTable.lastModifiedStatus]

        )


    }
    fun createFeedback(feedback: CreationFeedback, userLoggedInDetails : UserLoggedInDetails): Long {
        val employeeId = if (feedback.isAnonymous) null else userLoggedInDetails.employeeId

       return sql.insertInto(feedBackTable)
            .set(feedBackTable.companyId, userLoggedInDetails.companyId)
            .set(feedBackTable.employeeId, employeeId)
            .set(feedBackTable.content, feedback.content)
            .set(feedBackTable.isAnonymous, feedback.isAnonymous)
           .set(feedBackTable.status, Status.UNREVIEWED.toString())
            .returningResult(feedBackTable.id)
            .fetchOne()!!
            .get(feedBackTable.id)
    }

    fun getAllFeedbacks(userLoggedInDetails: UserLoggedInDetails):List<Feedback> =
        sql.select(feedBackTable.id,feedBackTable.employeeId,feedBackTable.content,feedBackTable.isAnonymous,feedBackTable.timeSubmitted,feedBackTable.status,feedBackTable.lastModifiedStatus)
            .from(feedBackTable)
            .where(feedBackTable.companyId.eq(userLoggedInDetails.companyId))
            .fetch(feedBackMapper)


    fun getFeedbackById(feedBackId : Long, userLoggedInDetails: UserLoggedInDetails): Feedback? =
        sql.select(feedBackTable.id,feedBackTable.employeeId,feedBackTable.content,feedBackTable.isAnonymous,feedBackTable.timeSubmitted,feedBackTable.status,feedBackTable.lastModifiedStatus)
            .from(feedBackTable)
            .where(feedBackTable.companyId.eq(userLoggedInDetails.companyId) , feedBackTable.id.eq(feedBackId))
            .fetchOne(feedBackMapper)


}
