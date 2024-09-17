package com.hibob.academy.service

import com.hibob.academy.dao.Owner
import com.hibob.academy.dao.OwnerCreationRequest
import com.hibob.academy.dao.OwnerDao
import jakarta.ws.rs.BadRequestException
import jakarta.ws.rs.NotFoundException


import org.springframework.stereotype.Component

@Component
class OwnerService(private val ownerDao: OwnerDao) {

    fun getOwners(companyId: Long): List<Owner> {
        return ownerDao.getOwners(companyId)
    }

    fun createOwner(ownerRequest: OwnerCreationRequest): Long {
        return ownerDao.createOwner(ownerRequest)

    }

    fun getOwnerById(id: Long, companyId: Long): Owner {
        return ownerDao.getOwnerById(id, companyId) ?: throw NotFoundException("Owner Not Found")
    }

    fun getOwnerByPetId(petId: Long, companyId: Long): Owner {
        return ownerDao.getOwnerByPetId(petId, companyId)
            ?: throw NotFoundException("Pet Not Found Or the pet dont has owner ")

    }

}