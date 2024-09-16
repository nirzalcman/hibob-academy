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

    @Test
    fun `validate getPetsByOwner when there are pets with the same ownerId - return list with the pets`(){
        val petId1 = dao.createPet(PetCreationRequest("Tomtom", "Dog", Date.valueOf(LocalDate.now()), companyId, ownerId))
        val petId2 = dao.createPet(PetCreationRequest("Dogidog", "Dog", Date.valueOf(LocalDate.now()), companyId, ownerId))
        val petId3 = dao.createPet(PetCreationRequest("Cticat", "Cat", Date.valueOf(LocalDate.now()), companyId, ownerId+1))

        val pet1 =Pet(petId1,"Tomtom", "Dog", Date.valueOf(LocalDate.now()), companyId, ownerId)
        val pet2 = Pet(petId2,"Dogidog", "Dog", Date.valueOf(LocalDate.now()), companyId, ownerId)
        assertEquals(listOf(pet1,pet2),dao.getPetsByOwner(ownerId))
    }

    @Test
    fun `validate getPetsByOwner when there are not pets with the same ownerId - return empty list`(){
        val petId1 = dao.createPet(PetCreationRequest("Tomtom", "Dog", Date.valueOf(LocalDate.now()), companyId, ownerId))
        assertEquals(emptyList<Pet>(), dao.getPetsByOwner(ownerId+1))
    }


    @Test
    fun `validate countPetsByType  returns the count of pets grouped by type`() {
        dao.createPet(PetCreationRequest("Tomtom", "Dog", Date.valueOf(LocalDate.now()), companyId, ownerId))
        dao.createPet(PetCreationRequest("Dogidog", "Dog", Date.valueOf(LocalDate.now()), companyId, ownerId))
        dao.createPet(PetCreationRequest("Cticat", "Cat", Date.valueOf(LocalDate.now()), companyId, ownerId))

        assertEquals(mapOf("Dog" to 2, "Cat" to 1), dao.countPetsByType())
    }

    @Test
    fun `validate countPetsByType returns empty map when no pets are present`() {
        assertEquals(emptyMap<String, Int>(), dao.countPetsByType())
    }




    @Test
    fun `validate pets retrieval by companyId`() {
        val petId1 = dao.createPet(PetCreationRequest("dog1", "Dog", Date.valueOf(LocalDate.now()), companyId, ownerId))
        val petId2 = dao.createPet(PetCreationRequest("cat1", "Cat", Date.valueOf(LocalDate.now()), companyId, ownerId))
        val petId3 = dao.createPet(PetCreationRequest("cat2", "Cat", Date.valueOf(LocalDate.now()), 50000L, ownerId))

        val actualPets = dao.getPetsByCompanyId(companyId)
        val expectedPets = listOf(
            Pet(petId1, "dog1", "Dog", Date.valueOf(LocalDate.now()), companyId, ownerId),
            Pet(petId2, "cat1", "Cat", Date.valueOf(LocalDate.now()), companyId, ownerId)
        )
        assertEquals(expectedPets, actualPets)
    }


    @Test
    fun `validate that updating multiple pets by owner update the ownerId of the pets`() {
        val petId1 = dao.createPet(PetCreationRequest("Tomtom", "Dog", Date.valueOf(LocalDate.now()), companyId, 1L))
        val petId2 = dao.createPet(PetCreationRequest("Caticat", "Cat", Date.valueOf(LocalDate.now()), companyId, 2L))

        dao.adoptMultiplePetsByOwner(3L, companyId, listOf(petId1, petId2))

        val pet1 = dao.getPetById(petId1, companyId)
        val pet2 = dao.getPetById(petId2, companyId)
        assertEquals(3L, pet1?.ownerId)
        assertEquals(3L, pet2?.ownerId)
    }


    @Test
    fun `validate multiple pet adoption updates only relevant pets  `() {
        val petId1 = dao.createPet(PetCreationRequest("Tomtom", "Dog", Date.valueOf(LocalDate.now()), companyId, 1L))
        val petId2 = dao.createPet(PetCreationRequest("Caticat", "Cat", Date.valueOf(LocalDate.now()), companyId, 2L))
        val petId3 = dao.createPet(PetCreationRequest("Dogidog", "Cat", Date.valueOf(LocalDate.now()), companyId, 2L))


        dao.adoptMultiplePetsByOwner(3L, companyId, listOf(petId1,petId3))

        val pet1 = dao.getPetById(petId1, companyId)
        val pet2 = dao.getPetById(petId2, companyId)
        val pet3 = dao.getPetById(petId3, companyId)

        assertEquals(3L, pet1?.ownerId)
        assertEquals(3L,pet3?.ownerId)
        assertEquals(2L, pet2?.ownerId)
    }

    @Test
    fun `validate multiple pets are added successfully`() {
        val petCreationRequests = listOf(
            PetCreationRequest("dog1", "Dog", Date.valueOf(LocalDate.now()), companyId, ownerId),
            PetCreationRequest("cat1", "Cat", Date.valueOf(LocalDate.now()), companyId, ownerId),
            PetCreationRequest("snake1", "Snake", Date.valueOf(LocalDate.now()), companyId, ownerId)
        )
        dao.addMultiplePets(petCreationRequests)

        val actualPets = dao.getPetsByCompanyId(companyId)

        petCreationRequests.forEachIndexed { index, petRequest ->
            val actualPet = actualPets[index]

            assertEquals(petRequest.name, actualPet.name)
            assertEquals(petRequest.type, actualPet.type)
            assertEquals(petRequest.dateOfArivval, actualPet.dateOfArivval)
            assertEquals(petRequest.companyId, actualPet.companyId)
            assertEquals(petRequest.ownerId, actualPet.ownerId)
        }
    }


}





