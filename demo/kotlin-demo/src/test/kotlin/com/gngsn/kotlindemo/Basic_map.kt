package com.gngsn.kotlindemo;

import org.junit.jupiter.api.Test
import java.util.*

class Basic_map {

    @Test
    fun ex_map() {
        val binaryReps = TreeMap<Char, String>()
        for (c in 'A'..'F') {
            val binary = Integer.toBinaryString(c.code)
            binaryReps[c] = binary // Java에서 binary.put(c, binary) 와 동일
        }

        for ((letter, binary) in binaryReps) {
            println("$letter = $binary")
        }
    }

    @Test
    fun ex_list() {
        val list = arrayListOf ("10", "11", "1001")
        for ((index, element) in list.withIndex()) {
            println("$index: $element")
        }
    }
}
