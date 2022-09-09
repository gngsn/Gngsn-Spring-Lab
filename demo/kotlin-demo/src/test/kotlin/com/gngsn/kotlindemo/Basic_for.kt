package com.gngsn.kotlindemo;

import org.junit.jupiter.api.Test
import java.util.*

class Basic_for {
    // 순차적으로 수를 세면서 3으로 나눠떨어지는 수에 대해서는 Fizz, 5로 나눠떨어지는 수에 대해 서는 Buzz 라고 말해야 한다.
    // 어떤 수가 3과 5로 모두 나눠떨어지면 'FizzBuzz'라고 말해야 한다.
    fun fizzBuzz(i: Int) = when {
        i % 15 == 0 -> "FizzBuzz "
        i % 3 == 0 -> "Fizz "
        i % 5 == 0 -> "Buzz "
        else -> "$i "
    }

    @Test
    fun ex_iter_downTo() {
        // 100 downTo 1은 역방향 수열을 만든다 역방향 수열의 기본 증가 값은 -1
        for (i in 100 downTo 1 step 2) print(fizzBuzz(i))
    }

    private fun ex_iter(): TreeMap<Char, String> {
        val binaryReps = TreeMap<Char, String>()

        // for in .. 범위 기반 iteration
        for (c in 'A'..'F') {
            val binary = Integer.toBinaryString(c.code)
            binaryReps[c] = binary // Java에서 binary.put(c, binary) 와 동일
        }
        return binaryReps
    }

    @Test
    fun ex_iter_main() {
        ex_iter()
    }


    @Test
    fun ex_for_In_map() {
        val binaryReps = ex_iter()

        // for..in 구문에 map을 쓰면 (key, value) 값 반환
       for ((letter, binary) in binaryReps) {
            println("$letter = $binary")
        }
    }

    @Test
    fun ex_for_in_list() {
        val list = arrayListOf ("10", "11", "1001")

        // withIndex: 인덱스와 함께 컬렉션을 이터레이션
        for ((index, element) in list.withIndex()) {
            println("$index: $element")
        }
    }
}
