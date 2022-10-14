package com.gngsn.kotlindemo.ch5

import com.gngsn.kotlindemo.ch5.LambdaExpression.Person
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SequenceCollection {

    @Test
    fun collectionVsSequence() {
        val result1 = (1..4)
            .map { print("map($it) ") ; it * it }
            .filter { print ("filter($it) "); it % 2 == 0 }

        val result2 = (1..4)
            .asSequence()
            .map { print("map($it) ") ; it * it }
            .filter { print ("filter($it) "); it % 2 == 0 }
            .toList()

        Assertions.assertEquals(result1, result2)
    }
    @Test
    fun collectionVsSequence2() {
        val people = listOf(Person("Alice", 29), Person("Bob", 31))

        // 컬렉션 API
        val result1 = people.map(Person::name).filter { it.startsWith("A") }

        val result2 = people.asSequence()         // 원본 컬렉션을 시퀀스로 변환
            .map(Person::name)                    //  시퀀스도 컬렉션과 똑같은 API 제공
            .filter { it.startsWith("A") }
            .toList()                             // 결과 시퀀스를 다시 리스트로 변환

        Assertions.assertEquals(result1, result2)
    }
}