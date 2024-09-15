package com.hibob.academy.dao

import com.hibob.academy.utils.BobDbTest
import jakarta.ws.rs.BadRequestException
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import kotlin.random.Random
import java.util.Date

@BobDbTest
class OwnerDaoTest @Autowired constructor(private val sql: DSLContext) {


    val table = OwnerTable.instance
    val petTable = PetTable.instance
    val ownerDao = OwnerDao(sql)
    val petDao = PetDao(sql)
    val companyId = Random.nextLong()
    private val ownerId = Random.nextLong()

    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(table).where(table.companyId.eq(companyId)).execute()
        sql.deleteFrom(petTable).where(petTable.companyId.eq(companyId)).execute()
    }

    @Test
    fun `validate that insert owners and read from the database`() {
        val idOwner = ownerDao.createOwner(OwnerCreationRequest( "nir", companyId, "1"))
        val owners = ownerDao.getOwners(companyId)
        assertEquals(listOf(Owner(idOwner, "nir", companyId, "1")), owners)

    }

    @Test
    fun `insert an owner with the same companyId and employeeId`() {
        val idOwner = ownerDao.createOwner(OwnerCreationRequest("nir", companyId, "1"))
        assertThrows(BadRequestException::class.java) {ownerDao.createOwner(OwnerCreationRequest("omer", companyId, "1"))}
    }

    @Test
    fun `read an owners when the db owner is empty`() {
        val owners = ownerDao.getOwners(companyId)
        assertEquals(emptyList<Owner>(), owners)
    }

    @Test
    fun `validate that owner is retrieved by id from the database`() {
        val id = ownerDao.createOwner(OwnerCreationRequest( "nir", companyId, "1"))
        val retrievedOwner = ownerDao.getOwnerById(id, companyId)

        assertNotNull(retrievedOwner)
        assertEquals("nir", retrievedOwner?.name)
        assertEquals(companyId, retrievedOwner?.companyId)
        assertEquals("1", retrievedOwner?.employId)
    }

    @Test
    fun `get owner by petId when pet has no owner`() {
        val petId = petDao.createPet( PetCreationRequest("Tomtom", "Dog", java.sql.Date.valueOf((LocalDate.now())), companyId, ownerId))
        val owner = ownerDao.getOwnerByPetId(petId+1, companyId)
        assertNull(owner)
    }


    @Test
    fun `get owner by petId and companyId when owner exists`() {
        val ownerId = ownerDao.createOwner(OwnerCreationRequest("Nir", companyId, "EMP001"))
        val petId = petDao.createPet( PetCreationRequest("Tomtom", "Dog", java.sql.Date.valueOf((LocalDate.now())), companyId, ownerId))
        val owner = ownerDao.getOwnerByPetId(petId, companyId)

        assertNotNull(owner)
        assertEquals("Nir", owner?.name)
        assertEquals(companyId, owner?.companyId)
        assertEquals("EMP001", owner?.employId)
    }


}