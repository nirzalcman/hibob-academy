package com.hibob.academy.employeeFeedback.dao


import org.jooq.DSLContext
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


    fun getRole(userLoggedInDetails: UserLoggedInDetails): Role {
        val roleString = sql.select(employeeTable.role)
            .from(employeeTable)
            .where(employeeTable.id.eq(userLoggedInDetails.employeeId))
            .and(employeeTable.companyId.eq(userLoggedInDetails.companyId))
            .fetchOne()
            ?.get(employeeTable.role)

        return roleString?.let {
            enumValueOf<Role>(it)
        } ?: throw IllegalStateException("Role is mandatory but not found for employee ID ${userLoggedInDetails.employeeId} and company ID ${userLoggedInDetails.companyId}")
    }

}
