package com.gngsn.kotlindemo.ch6.Example6

import org.junit.jupiter.api.Test

class People(
    val firstName: String,
    val lastName: String) {

    override fun equals(o: Any?): Boolean {
        val otherPerson = o as? People ?: return false
        return otherPerson.firstName == firstName && otherPerson.lastName == lastName
    } // 타입이 서로 일치하지  않으면 falsel 반환한다. 안전한 캐스트를 하고 나면 otherPerson 이 Person 타입으로 스마트 캐스트된다.
    
    override fun hashCode(): Int
        = firstName.hashCode() * 37 + lastName.hashCode()
}

class Example6 {
    fun strLen(s: String?): Int {
        return if (s != null) s.length else 0
    }

    fun <T> printHashCode(t: T) {
        println(t.hashCode())  // t"가 null이 될 수 있으므로 안전한 호출을 써야만 한다.
        println(t.toString())  // t"가 null이 될 수 있으므로 안전한 호출을 써야만 한다.
    }

    @Test fun test() {
        println("\n\n\n\n\n")
        printHashCode(null) // <— T의 타입은 "Any?”로 추론된다.
    }
}
