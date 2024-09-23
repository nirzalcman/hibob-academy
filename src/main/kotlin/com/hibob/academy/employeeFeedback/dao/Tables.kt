package com.hibob.academy.employeeFeedback.dao

import com.hibob.academy.utils.JooqTable



class EmployeeTable(tableName: String) : JooqTable(tableName) {
    val id = createBigIntField("id")
    val companyId = createBigIntField("company_id")
    val firstName = createVarcharField("first_name")
    val lastName = createVarcharField("last_name")
    val role = createVarcharField("role")
    val department = createVarcharField("department")

    companion object {
        val instance = EmployeeTable("employee")
    }
}


    class FeedbackTable(tableName: String) : JooqTable(tableName) {
        val id = createBigIntField("id")
        val companyId = createBigIntField("company_id")
        val employeeId = createBigIntField("employee_id")
        val content = createVarcharField("content")
        val isAnonymous = createBooleanField("is_anonymous")
        val timeSubmitted = createDateField("time_submitted")
        val status = createVarcharField("status")
        val lastModifiedStatus = createDateField("last_modified_status")

    companion object {
        val instance = FeedbackTable("feedback")
    }
}


class FeedbackResponseTable(tableName: String) : JooqTable(tableName) {
    val id = createBigIntField("id")
    val companyId = createBigIntField("company_id")
    val feedbackId = createBigIntField("feedback_id")
    val content = createVarcharField("content")
    val timeSubmitted = createDateField("time_submitted")
    val responseBy = createBigIntField("response_by")

    companion object {
        val instance = FeedbackResponseTable("feedback_response")
    }

}

