package com.hibob.academy.dao

import jakarta.ws.rs.NotFoundException
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
    val ownerId: Long? = null
)

data class Pet(
    val id: Long,
    val name: String,
    val type: String,
    val dateOfArivval: Date,
    val companyId: Long,
    val ownerId: Long?
)


@Component
class PetDao(private val sql: DSLContext) {
    private val table = PetTable.instance


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


    fun getPetsByOwner(ownerId: Long): List<Pet> =
        sql.select(table.id, table.name, table.type, table.dateOfArrival, table.companyId, table.ownerId)
            .from(table)
            .where(table.ownerId.eq(ownerId))
            .fetch(petMapper)


    fun countPetsByType(): Map<String, Int> =
        sql.select(table.type, DSL.count())
            .from(table)
            .groupBy(table.type)
            .fetch()
            .intoMap(table.type, DSL.count())

    fun getPetsByCompanyId(companyId: Long): List<Pet> =
        sql.select(table.id, table.name, table.type, table.dateOfArrival, table.companyId, table.ownerId)
            .from(table)
            .where(table.companyId.eq(companyId))
            .fetch(petMapper)


    fun adoptMultiplePetsByOwner(ownerId: Long, companyId: Long, petIds: List<Long>) =
        sql.update(table)
            .set(table.ownerId, ownerId)
            .where(table.id.`in`(petIds))
            .and(table.companyId.eq(companyId))
            .execute()


    fun addMultiplePets(petCreationRequests: List<PetCreationRequest>): IntArray {
        val insert = sql.insertInto(table)
            .columns(table.name, table.type, table.companyId, table.ownerId)
            .values(DSL.param(table.name), DSL.param(table.type), DSL.param(table.companyId), DSL.param(table.ownerId))

        val batch = sql.batch(insert)
        petCreationRequests.forEach {
            batch.bind(it.name, it.type, it.companyId, it.ownerId)
        }
        return batch.execute()
    }

    fun deleteByCompanyIds(companyIds: List<Long>) {
        sql.deleteFrom(PetTable.instance)
            .where(table.companyId.`in`(companyIds))
            .execute()
    }


}








