package com.hibob.academy.employeeFeedback.dao


import org.jooq.RecordMapper
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.SQL
import org.springframework.stereotype.Component

@Component
class FeedbackResponseDao(private val sql: DSLContext) {
    private val feedbackResponseTable = FeedbackResponseTable.instance

    private val feedbackResponseMapper = RecordMapper<Record, Response> { record ->
        Response(
            id = record[feedbackResponseTable.id],
            feedbackId = record[feedbackResponseTable.feedbackId],
            content = record[feedbackResponseTable.content],
            timeSubmitted = record[feedbackResponseTable.timeSubmitted],
            responseBy = record[feedbackResponseTable.responseBy],
        )
    }

    fun createResponse(userLoggedInDetails: UserLoggedInDetails, response: CreationResponse): Long =

        sql.insertInto(feedbackResponseTable)
            .set(feedbackResponseTable.companyId, userLoggedInDetails.companyId)
            .set(feedbackResponseTable.feedbackId, response.feedbackId)
            .set(feedbackResponseTable.content, response.content)
            .set(feedbackResponseTable.responseBy, userLoggedInDetails.employeeId)
            .onConflict(feedbackResponseTable.companyId,feedbackResponseTable.feedbackId, feedbackResponseTable.responseBy)
            .doUpdate()
            .set(feedbackResponseTable.content, response.content)
            .returningResult(feedbackResponseTable.id)
            .fetchOne()!!
            .get(feedbackResponseTable.id)

    fun getResponseById(userLoggedInDetails: UserLoggedInDetails,responseId: Long) : Response? =
        sql.select(feedbackResponseTable.id , feedbackResponseTable.feedbackId,feedbackResponseTable.content,feedbackResponseTable.timeSubmitted,feedbackResponseTable.responseBy)
            .from(feedbackResponseTable)
            .where(feedbackResponseTable.companyId.eq(userLoggedInDetails.companyId),feedbackResponseTable.id.eq(responseId))
            .fetchOne(feedbackResponseMapper)
}
