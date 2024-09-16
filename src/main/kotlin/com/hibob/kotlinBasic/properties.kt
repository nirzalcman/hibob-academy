package com.hibob.kotlinBasic

import java.time.DayOfWeek

/*
1. create class called Store that initlize by day and list of products
2. add property to that indicate if the store is open the store is open all the day expect saturday
3. add property to that indicate number of product
4. add val that get receipts //no need to implement the method but its an heavy task
5. I want to count number of calling get receipts
6. write vairable that initilize only when calling create method
Collapse
*/



data class Store(val day: DayOfWeek, val products: List<String>) {
    val isOpen = day != DayOfWeek.SATURDAY
    val numberOfProducts = products.size
    var counterRecepitsCalls = 0
    val recepits: List<String>
        get() {
            println("heavy function")
            counterRecepitsCalls++
            return listOf("rec1", "rec2", "rec3")
        }
    lateinit var vairable: String


    fun create() {
        vairable = "just now initilize"
    }

}
/*
fun main() {
    val s = Store(DayOfWeek.SUNDAY, listOf("bla"))
    s.create()
}
*/


























