package com.hibob.academy.employeeFeedback.dao

import org.jooq.RecordMapper
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.stereotype.Component

@Component
class EmployeeDao(private val sql: DSLContext) {
    private val employeeTable = EmployeeTable.instance

    fun createEmployee(userLoggedInDetails: UserLoggedInDetails, creationEmployee: CreationEmployee): Long =
        sql.insertInto(employeeTable)
            .set(employeeTable.companyId, userLoggedInDetails.companyId)
            .set(employeeTable.firstName, creationEmployee.firstName)
            .set(employeeTable.lastName, creationEmployee.lastName)
            .set(employeeTable.role, creationEmployee.role.toString())
            .set(employeeTable.department, creationEmployee.department.toString())
            .returning(employeeTable.id)
            .fetchOne()!!
            .get(employeeTable.id)

}
