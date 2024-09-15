package com.hibob.academy.dao

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


    companion object {
        val ownerMapper = RecordMapper<Record, Owner> { record ->
            Owner(
                id = record[OwnerTable.instance.id],
                name = record[OwnerTable.instance.name],
                companyId = record[OwnerTable.instance.companyId],
                employId = record[OwnerTable.instance.employId]
            )
        }
    }

    fun getOwners(companyId: Long): List<Owner> =
        sql.select(table.id, table.name, table.companyId, table.employId)
            .from(table)
            .where(table.companyId.eq(companyId))
            .fetch(ownerMapper)


    fun createOwner(owner: OwnerCreationRequest): Long {
        val res = sql.insertInto(table)
            .set(table.name, owner.name)
            .set(table.companyId, owner.companyId)
            .set(table.employId, owner.employId)
            .onConflict(table.companyId, table.employId)
            .doNothing()
            .returningResult(table.id)
            .fetchOne()

        return res?.get(table.id)?: 0L


    }

    fun getOwnerById(id: Long, companyId: Long): Owner? =
        sql.select(table.id, table.name, table.companyId, table.employId)
            .from(table)
            .where(table.id.eq(id), table.companyId.eq(companyId))
            .fetchOne(ownerMapper)
}