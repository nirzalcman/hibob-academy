package com.hibob.academy.service

import com.hibob.academy.dao.Owner
import com.hibob.academy.dao.OwnerCreationRequest
import com.hibob.academy.dao.OwnerDao
import com.hibob.academy.dao.PetDao
import jakarta.ws.rs.NotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.random.Random

class OwnerServiceTest {
    private val ownerDaoMock = mock<OwnerDao>()
    private val ownerService = OwnerService(ownerDaoMock)
    private val companyId = Random.nextLong()
    private val petId = Random.nextLong()


    @Test
    fun `getOwners should return a list of owners`() {
        val owners = listOf(
            Owner(1L, "Nir", companyId, "emp12"),
            Owner(2L, "Omer", companyId, "emp123")
        )
        whenever(ownerDaoMock.getOwners(companyId)).thenReturn(owners)

        val result = ownerService.getOwners(companyId)

        assertEquals(owners, result)
        verify(ownerDaoMock).getOwners(companyId)
    }


    @Test
    fun `createOwner should return the created owner's ID`() {
        val ownerRequest = OwnerCreationRequest("Nir", companyId, "emp12")
        val expectedId = 1L
        whenever(ownerDaoMock.createOwner(ownerRequest)).thenReturn(expectedId)

        val result = ownerService.createOwner(ownerRequest)

        assertEquals(expectedId, result)
        verify(ownerDaoMock).createOwner(ownerRequest)
    }


    @Test
    fun `getOwnerById should return an owner when found`() {
        val owner = Owner(1L, "Nir", companyId, "emp12")
        whenever(ownerDaoMock.getOwnerById(1L, companyId)).thenReturn(owner)

        val result = ownerService.getOwnerById(1L, companyId)

        assertEquals(owner, result)
        verify(ownerDaoMock).getOwnerById(1L, companyId)
    }

    @Test
    fun `getOwnerById should throw NotFoundException when owner not found`() {
        whenever(ownerDaoMock.getOwnerById(1L, companyId)).thenReturn(null)

        assertThrows(NotFoundException::class.java) {
            ownerService.getOwnerById(1L, companyId)
        }
        verify(ownerDaoMock).getOwnerById(1L, companyId)
    }

    @Test
    fun `getOwnerByPetId should return an owner when found`() {
        val owner = Owner(1L, "Nir", companyId, "emp12")
        whenever(ownerDaoMock.getOwnerByPetId(petId, companyId)).thenReturn(owner)

        val result = ownerService.getOwnerByPetId(petId, companyId)

        assertEquals(owner, result)
        verify(ownerDaoMock).getOwnerByPetId(petId, companyId)
    }

    @Test
    fun `getOwnerByPetId should throw NotFoundException when pet not found or no owner`() {
        whenever(ownerDaoMock.getOwnerByPetId(petId, companyId)).thenReturn(null)

        assertThrows(NotFoundException::class.java) {
            ownerService.getOwnerByPetId(petId, companyId)
        }
        verify(ownerDaoMock).getOwnerByPetId(petId, companyId)
    }

}