package com.hibob.academy.employeeFeedback.dao

import org.junit.jupiter.api.Assertions.*
import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.random.Random

@BobDbTest
class EmployeeDaoTest @Autowired constructor(private val sql: DSLContext) {
    private val employeeTable = EmployeeTable.instance
    private val employeeDao = EmployeeDao(sql)

    private val companyId = Random.nextLong()
    private val employeeId = Random.nextLong()
    private val userLoggedInDetails = UserLoggedInDetails(employeeId, companyId)

    private val creationEmployee = CreationEmployee(
        firstName = "Hi",
        lastName = "Bob",
        role = Role.EMPLOYEE,
        department = Department.HR
    )

    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(employeeTable).where(employeeTable.companyId.eq(companyId)).execute()
    }

    @Test
    fun `create employee and verify ID is returned`() {

        val createdEmployeeId = employeeDao.createEmployee(userLoggedInDetails, creationEmployee)
        assertNotNull(createdEmployeeId)
    }

    @Test
    fun `get role by employee ID`() {

        val createdEmployeeId = employeeDao.createEmployee(userLoggedInDetails, creationEmployee)
        val retrievedRole = employeeDao.getRole(userLoggedInDetails.copy(employeeId = createdEmployeeId))

        assertEquals(Role.EMPLOYEE, retrievedRole)
    }

}