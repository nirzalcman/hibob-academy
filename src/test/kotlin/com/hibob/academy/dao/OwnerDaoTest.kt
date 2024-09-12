package com.hibob.academy.dao

import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.random.Random

@BobDbTest
class OwnerDaoTest @Autowired constructor(private val sql: DSLContext) {

    val table = OwnerTable.instance
    val ownerDao = OwnerDao(sql)
    val companyId = Random.nextLong()

    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(table).where(table.companyId.eq(companyId)).execute()
    }

    @Test
    fun `validate that insert owners and read from the database`() {
        val idOwner = ownerDao.createOwner(Owner(null, "nir", companyId, "1"))
        val owners = ownerDao.getOwners(companyId)
        assertEquals(listOf(Owner(idOwner, "nir", companyId, "1")), owners)

    }

    @Test
    fun `insert an owner with the same companyId and employeeId`() {
        val idOwner = ownerDao.createOwner(Owner(null, "nir", companyId, "1"))
        assertEquals(0, ownerDao.createOwner(Owner(null, "omer", companyId, "1")))
    }

    @Test
    fun `read an owners when the db owner is empty`() {
        val owners = ownerDao.getOwners(companyId)
        assertEquals(emptyList<Owner>(), owners)
    }

    @Test
    fun `validate that owner is retrieved by id from the database`() {
        val id = ownerDao.createOwner(Owner(null, "nir", companyId, "1"))
        val retrievedOwner = ownerDao.getOwnerById(id, companyId)

        assertNotNull(retrievedOwner)
        assertEquals("nir", retrievedOwner?.name)
        assertEquals(companyId, retrievedOwner?.companyId)
        assertEquals("1", retrievedOwner?.employId)
    }


}