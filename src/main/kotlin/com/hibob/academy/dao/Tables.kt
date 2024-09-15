package com.hibob.academy.dao

import com.hibob.academy.utils.JooqTable
import javassist.CtMethod.ConstParameter.integer

class PetTable (tableName : String) : JooqTable(tableName){
    val id = createBigIntField("id")
    val name = createVarcharField("name")
    val type = createVarcharField("type")
    val dateOfArrival = createDateField("date_of_arrival")
    val companyId = createBigIntField("company_id")
    val ownerId = createBigIntField("owner_id")

    companion object {
        val instance = PetTable("pet")
    }

}


class OwnerTable (tableName : String) : JooqTable(tableName) {
    val id = createBigIntField("id")
    val name = createVarcharField("name")
    val companyId = createBigIntField("company_id")
    val employId = createVarcharField("employee_id")

    companion object {
        val instance = OwnerTable("owner")
    }
}