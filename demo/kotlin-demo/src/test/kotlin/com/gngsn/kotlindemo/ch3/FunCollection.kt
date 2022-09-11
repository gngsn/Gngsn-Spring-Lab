package com.gngsn.kotlindemo.ch3

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FunCollection {

    @Test
    fun collection_create() {
        val set = hashSetOf(1, 7, 53)
        val list = arrayListOf<Number>(1, 7, 53)

        /* to 는 일반 함수
            - Tuples.kt
            public infix fun <A, B> A.to(that: B): Pair<A, B> = Pair(this, that)
        */
        val map = hashMapOf<Number, String>(1 to "one", 7 to "seven", 53 to "fifty-three")

        println(set.javaClass)
        println(list.javaClass)
        println(map.javaClass)


        list.get(index = 2)
        // list.ensureCapacity(minCapacity= 2) <-- java method: Named arguments are not allowed for non-Kotlin functions
    }

    @Test
    fun collection_fun() {
        val strings = listOf("first", "second", "fourteenth")
        Assertions.assertEquals("fourteenth", strings.last())

        val numbers = setOf(1, 14, 2)
        Assertions.assertEquals(14, numbers.maxOrNull())
    }
}