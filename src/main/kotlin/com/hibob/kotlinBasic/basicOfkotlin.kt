package com.hibob.kotlinBasic

fun main() {
    helloWorld()
    evenOrOdd(3)
    println(checkEqual(4, 3))
    println(checkEqual(4, 4))
    max(5, 4)

    // a=1 b=5
    println(multiplication(b = 5))
    println(multiplication(a = 1, b = 5))
    //a=1 b=1
    println(multiplication())
    println(multiplication(a = 1, b = 1))
    println(multiplication(a = 1))
    println(multiplication(b = 1))
    //a=3 b=2
    println(multiplication(a = 3, b = 2))

}

fun helloWorld() {
    println("Hello World!")
}

fun evenOrOdd(number: Int) {
    println(if (number % 2 == 0) "Even" else "Odd")
}

fun checkEqual(number1: Int, number2: Int): Boolean {
    return number1 == number2
}

fun max(number1: Int, number2: Int) = println(if (number1 > number2) number1 else number2)


fun multiplication(a: Int = 1, b: Int = 1): Int = a * b

