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
    private val employeeTable = EmployeeTable.instance

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

    fun createFeedback(feedback: CreationFeedback, userLoggedInDetails: UserLoggedInDetails): Long {
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

    fun getAllFeedbacks(companyId: Long): List<Feedback> =
        sql.select(
            feedBackTable.id,
            feedBackTable.employeeId,
            feedBackTable.content,
            feedBackTable.isAnonymous,
            feedBackTable.timeSubmitted,
            feedBackTable.status,
            feedBackTable.lastModifiedStatus
        )
            .from(feedBackTable)
            .where(feedBackTable.companyId.eq(companyId))
            .fetch(feedBackMapper)


    fun getFeedbackById(feedBackId: Long, companyId: Long): Feedback? =
        sql.select(
            feedBackTable.id,
            feedBackTable.employeeId,
            feedBackTable.content,
            feedBackTable.isAnonymous,
            feedBackTable.timeSubmitted,
            feedBackTable.status,
            feedBackTable.lastModifiedStatus
        )
            .from(feedBackTable)
            .where(feedBackTable.companyId.eq(companyId), feedBackTable.id.eq(feedBackId))
            .fetchOne(feedBackMapper)


    fun searchFeedbacks(filter: FeedbackFilter, companyId: Long): List<Feedback> {

        return sql.select(
            feedBackTable.id,
            feedBackTable.employeeId,
            feedBackTable.content,
            feedBackTable.isAnonymous,
            feedBackTable.timeSubmitted,
            feedBackTable.status,
            feedBackTable.lastModifiedStatus
        )
            .from(feedBackTable)
            .leftJoin(employeeTable).on(feedBackTable.employeeId.eq(employeeTable.id))
            .where(
                feedBackTable.companyId.eq(companyId)
                    .let { initialCondition ->
                        filter.department?.let { initialCondition.and(employeeTable.department.eq(it)) }
                            ?: initialCondition
                    }
                    .let { condition ->
                        filter.afterDate?.let { condition.and(feedBackTable.timeSubmitted.greaterOrEqual(Date.valueOf(it))) }
                            ?: condition
                    }
                    .let { condition ->
                        filter.beforeDate?.let { condition.and(feedBackTable.timeSubmitted.lessThan(Date.valueOf(it))) }
                            ?: condition
                    }
                    .let { condition ->
                        filter.isAnonymous?.let { condition.and(feedBackTable.isAnonymous.eq(it)) } ?: condition
                    }
            )
            .fetch(feedBackMapper)
    }

    fun updateStatus(status: Status, feedbackId: Long, userLoggedInDetails: UserLoggedInDetails): Int =
        sql.update(feedBackTable)
            .set(feedBackTable.status, status.toString())
            .where(feedBackTable.id.eq(feedbackId))
            .and(feedBackTable.companyId.eq(userLoggedInDetails.companyId))
            .execute()


    fun getStatus(feedbackId: Long, userLoggedInDetails: UserLoggedInDetails): String? {
        return sql.select(feedBackTable.status)
            .from(feedBackTable)
            .where(feedBackTable.id.eq(feedbackId))
            .and(feedBackTable.companyId.eq(userLoggedInDetails.companyId))
            .and(feedBackTable.employeeId.eq(userLoggedInDetails.employeeId))
            .fetchOne()
            ?.get(feedBackTable.status)
    }
}
