package com.hibob.kotlinBasic

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Write unit tests to verify that the deposit and withdraw methods function correctly.
 * Handle edge cases, such as invalid inputs (e.g., negative amounts).
 * Ensure that the getBalance method returns the correct balance after a series of deposits and withdrawals.
 */

class BankAccountTest {

    @Test
    fun `deposit valid amount increases balance`() {
        val bankAccount = BankAccount(0.0)
        val balance = bankAccount.deposit(100.0)
        assertEquals(100.0, balance)
    }

    @Test
    fun `deposit negative or zero amount throws IllegalArgumentException`() {
        val bankAccount = BankAccount(0.0)
        assertThrows(IllegalArgumentException::class.java) { bankAccount.deposit(-1.0) }

    }

    @Test
    fun `withdraw valid amount decreases balance`() {
        val bankAccount = BankAccount(20.0)
        val balance = bankAccount.withdraw(10.0)
        assertEquals(10.0, balance)
    }

    @Test
    fun `withdraw amount greater than balance throws IllegalArgumentException`() {
        val bankAccount = BankAccount(20.0)
        assertThrows(IllegalArgumentException::class.java) { bankAccount.withdraw(21.0) }
    }

    @Test
    fun `withdraw negative or zero amount throws IllegalArgumentException`() {
        val bankAccount = BankAccount(20.0)
        assertThrows(IllegalArgumentException::class.java) { bankAccount.withdraw(-1.0) }
        assertThrows(IllegalArgumentException::class.java) { bankAccount.withdraw(0.0) }
    }

    @Test
    fun `getBalance returns the correct balance`() {
        val bankAccount = BankAccount(20.0)
        val balance = bankAccount.getBalance()
        assertEquals(20.0, balance)
    }
}
