package com.gngsn.kotlindemo.ch5.PredicateCollection

import com.gngsn.kotlindemo.ch5.LambdaExpression.Person
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PredicateCollection {

    @Test
    fun allTest() {
        val people = listOf(Person("Alice", 27), Person("Bob", 31))
        val canBeInClub27 = { p: Person -> p.age <= 27 }

        Assertions.assertFalse(people.all(canBeInClub27))
    }

    @Test
    fun anyTest() {
        val people = listOf(Person("Alice", 27), Person("Bob", 31))
        val canBeInClub27 = { p: Person -> p.age <= 27 }

        Assertions.assertTrue(people.any(canBeInClub27))
    }

    @Test
    fun deMorgansTheorem() {
        val list = listOf (1, 2, 3)
        println(!list.all { it == 3 }) //`!`를 눈치 채지 못하는 경우가 자주 있다. 따라서 이런 식보다는 any를 사용하는 식이 더 낫다.
        println(list.any { it != 3 }) // any를 사용하려면 술어를 부정해야 한다.

        Assertions.assertEquals(!list.all { it == 3 }, list.any { it != 3 })
    }

    @Test
    fun countTest() {
        val people = listOf (Person("Alice", 27), Person("Bob", 31))
        val canBeInClub27 = { p: Person -> p.age <= 27 }

        Assertions.assertEquals(1, people.count(canBeInClub27))
        Assertions.assertEquals(1, people.filter(canBeInClub27).size) // 중간 컬렉션이 생긴다
    }

    @Test
    fun findTest() {
        val people = listOf (Person("Alice", 27), Person("Bob", 31))
        val canBeInClub27 = { p: Person -> p.age <= 27 }

        Assertions.assertEquals(Person("Alice", 27), people.find(canBeInClub27))
    }
    @Test
    fun groupByTest() {
        val people = listOf (Person("Alice", 31), Person("Bob", 29), Person("Carol", 31))
        println(people.groupBy { it.age })

        Assertions.assertEquals(
            mapOf(29 to listOf(Person(name="Bob", age=29)), 31 to listOf(Person(name="Alice", age=31), Person(name="Carol", age=31))),
            people.groupBy { it.age })
    }

    @Test
    fun groupByStringTest() {
        val list = listOf ("a", "ab", "b")
        val result = list.groupBy(String::first) // {a=[a, ab], b=[b]}

        Assertions.assertEquals(mapOf('a' to listOf("a", "ab"), 'b' to listOf("b")), result)
    }

    @Test
    fun intermediateNotRunTest() {
        listOf(1, 2, 3, 4).asSequence ()
            .map { print("map($it) "); it * it }
            .filter { print ("filter($it) "); it % 2 == 0 } // print nothing
    }

    @Test
    fun intermediateRunTest() {
        listOf(1, 2, 3, 4).asSequence ()
            .map { print("map($it) "); it * it }
            .filter { print ("filter($it) "); it % 2 == 0 }
            .toList() // map(1) filter(1) map(2) filter(4) map(3) filter(9) map(4) filter(16)
    }
}