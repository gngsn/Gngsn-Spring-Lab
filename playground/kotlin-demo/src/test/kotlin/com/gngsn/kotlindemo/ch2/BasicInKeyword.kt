package com.gngsn.kotlindemo.ch2;

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BasicInKeyword {

    fun isLetter(c: Char) = c in 'a'..'z' || c in 'A'..'Z'
    fun isNotDigit(c: Char) = c !in '0'..'9'  // '0'<= c && x<= '9' 로 변환된다

    @Test
    fun ex_range() {
        val oneToTen = 1..10
        println("oneToTen : $oneToTen")      // 1..10
        println("class: ${oneToTen.javaClass}")  // class kotlin.ranges.IntRange

        val aToZ = 'a'..'z'
        println("oneToTen : $aToZ")      // a..z
        println("class: ${aToZ.javaClass}")  // class kotlin.ranges.CharRange

        // comparable 인터페이스를 구현한 클래스로 범위 생성
        val comparableRange = "Java".."Scala"           // "Java" <= "Kotlin" && "Kotlin” 〈= "Scala"와 동일
        println("oneToTen : $comparableRange")          // Java..Scala
        println("class: ${comparableRange.javaClass}")  // class kotlin.ranges.ComparableRange
    }

    @Test
    fun ex_for_In() {
        Assertions.assertTrue(isLetter('q'))
        Assertions.assertTrue(isNotDigit('x'))
    }

    private fun recognize(c: Char) = when (c) {
        in '0'..'9' -> "It's a digit!"
        in 'a'..'z', in 'A'..'Z' -> "It's a letter!"
        else -> "I don't know..."
    }

    @Test
    fun ex_when_In() {
        Assertions.assertEquals("It's a digit!", recognize('8'))
        Assertions.assertEquals("It's a letter!", recognize('x'))
    }

    @Test
    fun ex_set_In() {
        Assertions.assertFalse("Kotlin" in setOf("Java", "Scala"))
    }
}
