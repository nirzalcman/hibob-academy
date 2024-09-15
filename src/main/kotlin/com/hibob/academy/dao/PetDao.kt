package com.hibob.academy.dao

import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.RecordMapper
import org.jooq.impl.DSL
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.time.LocalDateTime
import java.sql.Date

data class PetCreationRequest(
    val name: String,
    val type: String,
    val dateOfArivval: Date,
    val companyId: Long,
    val ownerId: Long
)

data class Pet(
    val id: Long,
    val name: String,
    val type: String,
    val dateOfArivval: Date,
    val companyId: Long,
    val ownerId: Long
)


@Component
class PetDao(private val sql: DSLContext) {
    private val table = PetTable.instance
    private val ownerTable = OwnerTable.instance
    private val ownerDao = OwnerDao(sql)


    private val petMapper = RecordMapper<Record, Pet> { record ->
        Pet(
            record[table.id],
            record[table.name],
            record[table.type],
            record[table.dateOfArrival],
            record[table.companyId],
            record[table.ownerId]
        )
    }

    fun getPetsByType(type: String, companyId: Long): List<Pet> =
        sql.select(table.id, table.name, table.type, table.dateOfArrival, table.companyId, table.ownerId)
            .from(table)
            .where(table.type.eq(type), table.companyId.eq(companyId))
            .fetch(petMapper)


    fun createPet(pet: PetCreationRequest): Long =
         sql.insertInto(table)
            .set(table.name, pet.name)
            .set(table.companyId, pet.companyId)
            .set(table.type, pet.type)
            .set(table.ownerId, pet.ownerId)
             .returningResult(table.id)
             .fetchOne()!!
             .get(table.id)



    fun getPetById(petId: Long, companyId: Long): Pet? =
        sql.select(table.id, table.name, table.type, table.dateOfArrival, table.companyId, table.ownerId)
            .from(table)
            .where(table.id.eq(petId), table.companyId.eq(companyId))
            .fetchOne(petMapper)


    fun updatePetOwner(petId: Long, ownerId: Long, companyId: Long): Int =
        sql.update(table)
            .set(table.ownerId, ownerId)
            .where(table.id.eq(petId), table.companyId.eq(companyId))
            .execute()


    fun getOwnerByPetId(petId: Long, companyId: Long): Owner? {
        return sql.select(ownerTable.id, ownerTable.name, ownerTable.companyId, ownerTable.employId)
            .from(table)
            .join(ownerTable)
            .on(table.ownerId.eq(ownerTable.id))
            .where(table.id.eq(petId), table.companyId.eq(companyId))
            .fetchOne(ownerDao.ownerMapper)
    }


    }








