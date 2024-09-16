package com.hibob.academy.service

import com.hibob.academy.dao.Pet
import com.hibob.academy.dao.PetCreationRequest
import com.hibob.academy.dao.PetDao
import jakarta.ws.rs.NotFoundException
import org.springframework.stereotype.Component

@Component
class PetService(private val petDao: PetDao) {

    fun createPet(petCreationRequest: PetCreationRequest): Long {
        return petDao.createPet(petCreationRequest)
    }

    fun getPetsByType(type: String, companyId: Long): List<Pet> {
        return petDao.getPetsByType(type, companyId)
    }

    fun getPetById(id: Long, companyId: Long): Pet? {
        return petDao.getPetById(id, companyId)
    }

    fun updatePetOwner(petId: Long, ownerId: Long, companyId: Long): Int {
        return if (petDao.updatePetOwner(petId, ownerId, companyId) > 0) 1 else throw NotFoundException("Pet Not Found")

    }
}
