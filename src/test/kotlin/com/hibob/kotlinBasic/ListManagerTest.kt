/**
 * Exercise Instructions:
 *
 * Write tests for addPerson method:
 *
 * Test adding a unique person.
 * Test adding a duplicate person and ensure it throws the expected exception.
 * Test adding multiple people, checking that the list grows appropriately.
 *
 *
 * Write tests for removePerson method:
 *
 * Test removing a person that exists in the list.
 * Test trying to remove a person that does not exist, ensuring it returns false.
 * Test the state of the list after multiple add and remove operations.
 *
 *
 * Write tests for getPeopleSortedByAgeAndName method:
 *
 * Test with an empty list.
 * Test with one person.
 * Test with multiple people to ensure they are sorted first by age, then by name.
 * Test with edge cases like people with the same name but different ages and vice versa.
 *
 */

package com.hibob.kotlinBasic

import ListManager
import Person
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ListManagerTest {


    @Test
    fun `add unique person - the new person in the list of persons`() {
        val listManager = ListManager()
        val newPerson = Person("nir", 27)
        listManager.addPerson(newPerson)
        assertTrue(listManager.getPeople().contains(newPerson))
    }

    @Test
    fun `adding a duplicate person throws IllegalArgumentException`() {
        val listManager = ListManager()
        listManager.addPerson(Person("nir", 27))
        assertThrows(IllegalArgumentException::class.java) { listManager.addPerson(Person("nir", 27)) }
    }

    @Test
    fun `adding multiple people - the list grows appropriately`() {
        val listManager = ListManager()
        val sizeBefore = listManager.getPeople().size
        listManager.addPerson(Person("nir", 27))
        listManager.addPerson(Person("omer", 28))
        listManager.addPerson(Person("tomer", 28))
        assertEquals(sizeBefore + 3, listManager.getPeople().size)
    }


    @Test
    fun `removing a person that exists in the list`() {
        val listManager = ListManager()
        listManager.addPerson(Person("nir", 27))
        listManager.removePerson(Person("nir", 27))
        assertFalse(listManager.getPeople().contains(Person("nir", 27)))

    }

    @Test
    fun `trying to remove a person that does not exist - return false`() {
        val listManager = ListManager()
        val result = listManager.removePerson(Person("nir", 27))
        assertEquals(false, result)
    }

    @Test
    fun `Test the state of the list after multiple add and remove operations`() {
        val listManager = ListManager()
        listManager.addPerson(Person("nir", 27))
        listManager.addPerson(Person("omer", 29))
        listManager.removePerson(Person("nir", 27))
        listManager.addPerson(Person("tomer", 30))
        listManager.removePerson(Person("omer", 29))
        assertEquals(listManager.getPeople(), listOf(Person("tomer", 30)))

    }

    @Test
    fun `getPeopleSortedByAgeAndName with empty list - return empty list`() {
        val listManager = ListManager()
        assertTrue(listManager.getPeopleSortedByAgeAndName().isEmpty())
    }

    @Test
    fun `getPeopleSortedByAgeAndName with one person - return list with the person`() {
        val listManager = ListManager()
        listManager.addPerson(Person("nir", 27))
        assertEquals(listOf(Person("nir", 27)), listManager.getPeopleSortedByAgeAndName())
    }

    @Test
    fun `with multiple people to ensure they are sorted first by age, then by name`() {
        val listManager = ListManager()
        listManager.addPerson(Person("brother", 26))
        listManager.addPerson(Person("father", 50))
        listManager.addPerson(Person("mother", 50))
        val orderList = listManager.getPeopleSortedByAgeAndName()
        assertEquals(listOf(Person("brother", 26), Person("father", 50), Person("mother", 50)), orderList)
    }

    @Test
    fun `with edge cases like people with the same name but different ages and vice versa`() {
        val listManager = ListManager()
        listManager.addPerson(Person("nir", 27))
        listManager.addPerson(Person("nir", 28))
        listManager.addPerson(Person("omer", 26))
        listManager.addPerson(Person("tomer", 26))
        val orderList = listManager.getPeopleSortedByAgeAndName()
        assertEquals(listOf(Person("omer", 26), Person("tomer", 26), Person("nir", 27), Person("nir", 28)), orderList)
    }


    @Test
    fun `calculateStatistics without people - return null`() {
        val listManager = ListManager()
        assertNull(listManager.calculateStatistics())



    }

    @Test
    fun `calculateStatistics with 1 person `() {
        val listManager = ListManager()
        val person = Person("nir", 27)
        listManager.addPerson(person )

        val peopleStatistics = listManager.calculateStatistics()

        assertNotNull(peopleStatistics)

        peopleStatistics?.let {
            assertEquals(27.0, it.averageAge)
            assertEquals(person, it.youngest)
            assertEquals(person, it.oldest)
            assertEquals(mapOf(27 to 1), it.ageCount)

        }

}
    @Test
    fun `calculateStatistics with multiple persons`() {
        val listManager = ListManager()
        val person1 = Person("nir", 33)
        val person2 = Person("omer", 30)
        val person3 = Person("tomer", 27)

        listManager.addPerson(person1)
        listManager.addPerson(person2)
        listManager.addPerson(person3)

        val stats = listManager.calculateStatistics()

        assertNotNull(stats)
        stats?.let {
            assertEquals(30.0, it.averageAge)
            assertEquals(person1, it.oldest)
            assertEquals(mapOf(27 to 1, 30 to 1, 33 to 1), it.ageCount)
        }
    }















}