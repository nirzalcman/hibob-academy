package com.hibob.academy.employeeFeedback.dao

import java.sql.Date

enum class Role {
    HR,ADMIN,EMPLOYEE
}

enum class Department {
    IT, HR, Sales, Finance
}

enum class Status {
    REVIEWED,UNREVIEWED
}

data class  UserLoggedInDetails(
    val employeeId: Long,
    val companyId: Long,
    val role : Role
)

data class CreationFeedback(
    val content: String,
    val isAnonymous: Boolean,
)

data class FeedbackFilter(
    val department: String? = null,
    val afterDate: String? = null,
    val beforeDate: String? = null,
    val isAnonymous: Boolean? = null
)

data class Feedback(
    val id: Long,
    val employeeId: Long?,
    val content: String,
    val isAnonymous: Boolean,
    val timeSubmitted: Date,
    val status: Status,
    val lastModifiedStatus: Date?
)

data class CreationResponse (
    val id: Long,
    val content: String
)

data class Response (
    val id: Long,
    val feedbackId: Long,
    val content: String,
    val timeSubmitted: Date,
    val responseBy : Long
)


















