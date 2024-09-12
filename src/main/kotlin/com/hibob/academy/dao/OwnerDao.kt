package com.hibob.academy.dao

import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.RecordMapper
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.Date

data class Owner(val name: String, val companyId: Long, val employId: String)


@Component
class OwnerDao(private val sql: DSLContext) {
    private val table = OwnerTable.instance


    private val ownerMapper = RecordMapper<Record, Owner> { record ->
        Owner(record[table.name], record[table.companyId], record[table.employId])
    }

    fun getOwners(): List<Owner> =
        sql.select(table.name, table.companyId, table.employId)
            .from(table)
            .fetch(ownerMapper)


    fun createOwner(owner : Owner) : Long {
        val res = sql.insertInto(table)
            .set(table.name, owner.name)
            .set(table.companyId, owner.companyId)
            .set(table.employId, owner.employId)
            .onConflict(table.companyId, table.employId)
            .doNothing()
            .returningResult(table.id)
            .fetchOne()

        return res?.get(table.id) ?: 0
    }



    fun getOwnerById(id : Long) :Owner? =
        sql.select(table.name , table.companyId , table.employId)
            .from(table)
            .where(table.id.eq(id))
            .fetchOne(ownerMapper)

}