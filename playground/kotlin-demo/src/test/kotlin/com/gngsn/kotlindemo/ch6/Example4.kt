package com.gngsn.kotlindemo.ch6.Example4

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class Address(val streetAddress: String, val zipCode: Int, val city: String, val country: String)

class Company(val name: String, val address: Address?)

class Person(val name: String, val company: Company?)


class Example4 {
    fun Person.countryName(): String {
        return this.company?.address?.country ?: "Unknown"
    }

    fun printShippingLabel(person: Person) {
        val address = person.company?.address
            ?: throw IllegalArgumentException("No address") // 주소가 없으면 예외를 발생시킨다.

        with(address) {
            println(streetAddress) // address는 null이 아님
            println("$zipCode $city, $country")
        }
    }

    @Test
    fun test_countryName() {
        val person = Person("Dmitry", null)
        println(person.countryName())
        assertEquals(person.countryName(), "Unknown")
    }

    @Test
    fun test_printShippingLabel() {
        val address = Address("Elsestr. 47", 80687, "Munich", "Germany")
        val jetbrains = Company("JetBrains", address)
        val person = Person("Dmitry", jetbrains)

        printShippingLabel(person)
//        "Elsestr. 47"
//        "80687 Munich, Germany"
        assertThrows<IllegalArgumentException> { printShippingLabel(Person("Alexey", null)) }
    }
}