package com.hibob.academy.dao

import org.junit.jupiter.api.Assertions.*
import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.sql.Date
import java.time.LocalDate
import kotlin.random.Random

@BobDbTest
class PetDaoTest @Autowired constructor(private val sql: DSLContext) {

    private val ownerTable = OwnerTable.instance
    private val table = PetTable.instance
    private val dao = PetDao(sql)
    private val companyId = Random.nextLong()
    private val ownerId = Random.nextLong()


    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(table).where(table.companyId.eq(companyId)).execute()
        sql.deleteFrom(ownerTable).where(ownerTable.companyId.eq(companyId)).execute()
    }

    @Test
    fun `validate insert pet and retrieve it by type`() {
        val idPet =
            dao.createPet(PetCreationRequest("Tomtom", "Dog", Date.valueOf(LocalDate.now()), companyId, ownerId))
        val pets = dao.getPetsByType("Dog", companyId)
        assertEquals(listOf(Pet(idPet, "Tomtom", "Dog", Date.valueOf(LocalDate.now()), companyId, ownerId)), pets)

    }

    @Test
    fun `get by type when in the db not exists pets with this type`() {
        dao.createPet(PetCreationRequest("Tomtom", "Dog", Date.valueOf(LocalDate.now()), companyId, ownerId))
        val pets = dao.getPetsByType("Cat", companyId)
        assertEquals(emptyList<Pet>(), pets)
    }

    @Test
    fun `validate that updatePetOwner correctly update the ownerId of the pet`() {

        val initialOwnerId = 100L
        val newOwnerId = 200L

        val pet = PetCreationRequest("Tomtom", "Dog", Date.valueOf(LocalDate.now()), companyId, initialOwnerId)
        val petId = dao.createPet(pet)

        dao.updatePetOwner(petId, newOwnerId, companyId)
        val retrivePet = dao.getPetById(petId, companyId)

        assertEquals(newOwnerId, retrivePet?.ownerId)
    }

    @Test
    fun `validate that updating a non-existent pet returns 0`() {
        val petId =
            dao.createPet(PetCreationRequest("Tomtom", "Dog", Date.valueOf(LocalDate.now()), companyId, ownerId))
        val ownerId = 100L
        val res = dao.updatePetOwner(petId + 1, ownerId, companyId)

        assertEquals(0, res)
    }


}





