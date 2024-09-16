package com.hibob.academy.dao

import org.jooq.DSLContext
import org.springframework.stereotype.Component

@Component
class VaccineDao(private val sql: DSLContext) {
    private val table = VaccineTable.instance
}

