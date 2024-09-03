package com.hibob.kotlinBasic

fun List<Int>.sumNew() : Int {
    var sum  = 0
    for (number in this) sum+=number
    return sum
}

infix fun Number.toPowerOf(exponent: Number): Double{
    return Math.pow(this.toDouble() , exponent.toDouble())
}

fun main(args: Array<String>) {
    println(listOf(1, 2, 3, 4, 5).sumNew())
    println(5.5.toPowerOf(2))
}