package com.hibob.academy.dao

import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.RecordMapper
import org.jooq.impl.DSL
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.time.LocalDateTime
import java.sql.Date


data class Pet(
    val id: Long?,
    val name: String,
    val type: String,
    val dateOfArivval: Date,
    val companyId: Long,
    val ownerId: Long
)

data class PetWithOutType(val name: String, val dateOfArivval: Date, val companyId: Long)

@Component
class PetDao(private val sql: DSLContext) {
    private val table = PetTable.instance

    private val petWithOutTypeMapper = RecordMapper<Record, PetWithOutType> { record ->
        PetWithOutType(record[table.name], record[table.dateOfArrival], record[table.companyId])
    }

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

    fun getPetsByType(type: String, companyId: Long): List<PetWithOutType> =
        sql.select(table.name, table.dateOfArrival, table.companyId)
            .from(table)
            .where(table.type.eq(type), table.companyId.eq(companyId))
            .fetch(petWithOutTypeMapper)


    fun createPet(pet: Pet): Long {
        val res = sql.insertInto(table)
            .set(table.name, pet.name)
            .set(table.companyId, pet.companyId)
            .set(table.type, pet.type)
            .set(table.ownerId, pet.ownerId)
            .returningResult(table.id)
            .fetchOne()!!
        return res?.get(table.id) ?: 0
    }


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


}







