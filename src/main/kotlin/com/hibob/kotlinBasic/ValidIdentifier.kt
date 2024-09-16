package com.hibob.kotlinBasic
/*
fun main(args: Array<String>) {

    println("////////////////")
    println(isValidIdentifier("012"))
    println(isValidIdentifier("no$"))
    println(isValidIdentifier(""))
    println(isValidIdentifier("name"))
    println(isValidIdentifier("_name"))  // true
    println(isValidIdentifier("_12"))

}
*/


fun isValidIdentifier(s: String): Boolean {
    if (s.isEmpty()) return false
    if (s[0].isLetter() || s[0] == '_') {
        for (char in s){
            if (!(char.isLetterOrDigit() || char == '_')) return false
        }
        return true
    }
    return false
}