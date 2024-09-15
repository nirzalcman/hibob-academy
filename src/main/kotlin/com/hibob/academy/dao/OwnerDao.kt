package com.hibob.academy.dao

import jakarta.ws.rs.BadRequestException
import jakarta.ws.rs.NotFoundException
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.RecordMapper
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.Date

data class OwnerCreationRequest(val name: String, val companyId: Long, val employId: String)

data class Owner(val id: Long, val name: String, val companyId: Long, val employId: String)


@Component
class OwnerDao(private val sql: DSLContext) {
    private val table = OwnerTable.instance
    private val petTable = PetTable.instance


    val ownerMapper = RecordMapper<Record, Owner> { record ->
        Owner(record[table.id], record[table.name], record[table.companyId], record[table.employId])
    }


    fun getOwners(companyId: Long): List<Owner> =
        sql.select(table.id, table.name, table.companyId, table.employId)
            .from(table)
            .where(table.companyId.eq(companyId))
            .fetch(ownerMapper)


    fun createOwner(owner: OwnerCreationRequest): Long =
        sql.insertInto(table)
            .set(table.name, owner.name)
            .set(table.companyId, owner.companyId)
            .set(table.employId, owner.employId)
            .onConflict(table.companyId, table.employId)
            .doNothing()
            .returningResult(table.id)
            .fetchOne()?.get(table.id) ?: throw BadRequestException("Owner creation Failed")


    fun getOwnerById(id: Long, companyId: Long): Owner? =
        sql.select(table.id, table.name, table.companyId, table.employId)
            .from(table)
            .where(table.id.eq(id), table.companyId.eq(companyId))
            .fetchOne(ownerMapper) ?: throw NotFoundException("Owner Not Found")


    fun getOwnerByPetId(petId: Long, companyId: Long): Owner? =
        sql.select(table.id, table.name, table.companyId, table.employId)
            .from(petTable)
            .leftJoin(table)
            .on(petTable.ownerId.eq(table.id))
            .where(petTable.id.eq(petId), petTable.companyId.eq(companyId))
            .fetchOne(ownerMapper)

}