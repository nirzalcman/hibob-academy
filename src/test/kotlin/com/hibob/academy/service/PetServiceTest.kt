package com.hibob.academy.service

import com.hibob.academy.dao.Pet
import com.hibob.academy.dao.PetCreationRequest
import com.hibob.academy.dao.PetDao
import jakarta.ws.rs.NotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.sql.Date
import java.time.LocalDate
import kotlin.random.Random

class PetServiceTest {
    private val petDaoMock = mock<PetDao>()
    private val petService = PetService(petDaoMock)

    @Test
    fun `test createPet returns pet id`() {
        val petCreationRequest = PetCreationRequest("Tomtom", "Dog", Date.valueOf(LocalDate.now()), 1L, 10L)
        val expectedPetId = 1L
        whenever(petDaoMock.createPet(petCreationRequest)).thenReturn(expectedPetId)

        val result = petService.createPet(petCreationRequest)

        assertEquals(expectedPetId, result)
        verify(petDaoMock).createPet(petCreationRequest)
    }

    @Test
    fun `test getPetsByType returns list of pets`() {
        val type = "Dog"
        val companyId = 1L
        val expectedPets = listOf(Pet(1L, "Dogidog", "Dog", Date.valueOf(LocalDate.now()), companyId, 10L))
        whenever(petDaoMock.getPetsByType(type, companyId)).thenReturn(expectedPets)

        val result = petService.getPetsByType(type, companyId)

        assertEquals(expectedPets, result)
        verify(petDaoMock).getPetsByType(type, companyId)
    }

    @Test
    fun `test getPetById returns pet`() {
        val petId = 1L
        val companyId = 1L
        val expectedPet = Pet(1L, "Dogidog", "Dog", Date.valueOf(LocalDate.now()), companyId, 10L)
        whenever(petDaoMock.getPetById(petId, companyId)).thenReturn(expectedPet)

        val result = petService.getPetById(petId, companyId)

        assertEquals(expectedPet, result)
        verify(petDaoMock).getPetById(petId, companyId)
    }

    @Test
    fun `test getPetById returns null when pet not found`() {
        val petId = 1L
        val companyId = 1L
        whenever(petDaoMock.getPetById(petId, companyId)).thenReturn(null)

        val result = petService.getPetById(petId, companyId)

        assertNull(result)
        verify(petDaoMock).getPetById(petId, companyId)
    }

    @Test
    fun `test updatePetOwner updates pet owner`() {
        val petId = 1L
        val ownerId = 2L
        val companyId = 1L
        whenever(petDaoMock.updatePetOwner(petId, ownerId, companyId)).thenReturn(1)

        val result = petService.updatePetOwner(petId, ownerId, companyId)

        assertEquals(1, result)
        verify(petDaoMock).updatePetOwner(petId, ownerId, companyId)
    }

    @Test
    fun `test updatePetOwner throws NotFoundException when pet not found`() {
        val petId = 1L
        val ownerId = 2L
        val companyId = 1L
        whenever(petDaoMock.updatePetOwner(petId, ownerId, companyId)).thenReturn(0)

        assertThrows(NotFoundException::class.java) {
            petService.updatePetOwner(petId, ownerId, companyId)
        }
        verify(petDaoMock).updatePetOwner(petId, ownerId, companyId)
    }
}
