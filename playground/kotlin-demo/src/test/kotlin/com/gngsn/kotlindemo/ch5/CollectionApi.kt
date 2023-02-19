package com.gngsn.kotlindemo.ch5

import com.gngsn.kotlindemo.ch2.Basic
import com.gngsn.kotlindemo.ch5.LambdaExpression.Person
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CollectionApi {
    @Test
    fun filterTest() {
        val list = listOf(1, 2, 3, 4)
        val result = list.filter { it % 2 == 0 }

        Assertions.assertEquals(listOf(2, 4), result)
    }

    @Test
    fun objectFilterTest() {
        val people = listOf(Person("Alice", 29), Person("Bob", 31))
        val result = people.filter { it.age > 30 } // 30살 이상 filtering

        Assertions.assertEquals(listOf(Person(name = "Bob", age = 31)), result)
    }

    @Test
    fun mapTest() {
        val list = listOf(1, 2, 3, 4)
        val result = list.map { it * it }

        Assertions.assertEquals(listOf(1, 4, 9, 16), result)
    }

    @Test
    fun mapTest2() {
        val people = listOf(Person("Alice", 29), Person("Bob", 31))

        /* Person 객체의 이름만을 출력 */
        // example 1
        val result = people.map { it.name }
        Assertions.assertEquals(listOf("Alice", "Bob"), result)

        // example 2
        val result2 = people.map(Person::name)
        Assertions.assertEquals(listOf("Alice", "Bob"), result2)
    }

    @Test
    fun filterMapTest() {
        val people = listOf(Person("Alice", 29), Person("Bob", 31))
        val result = people.filter { it.age > 30 }.map(Person::name)

        Assertions.assertEquals(listOf("Bob"), result)
    }
}