package com.hibob.kotlinBasic


enum class CodeType {
    ZIPCODE , POSTCODE
}
data class Location(val country: String,val city: String,val street: String, val codeType : CodeType , val code : String )

