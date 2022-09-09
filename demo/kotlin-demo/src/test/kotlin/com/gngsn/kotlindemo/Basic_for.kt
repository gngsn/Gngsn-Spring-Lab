package com.gngsn.kotlindemo;

import org.junit.jupiter.api.Test

class Basic_for {
    // 순차적 으로 수를 세면서 3으로 나눠떨어지는 수에 대해서는 피즈, 5로 나눠떨어지는 수에 대해 서는 버즈라고 말해야 한다. 어떤 수가 3과 5로 모두 나눠떨어지면 ‘피즈버즈’라고 말해야 한다.
    fun fizzBuzz(i: Int) = when {
        i % 15 == 0 -> "FizzBuzz "
        i % 3 == 0 -> "Fizz "
        i % 5 == 0 -> "Buzz "
        else -> "$i "
    }

    @Test
    fun ex_iter() {
        // 100 downTo 1은 역방향 수열을 만든다 역방향 수열의 기본 증가 값은 -1
        for (i in 100 downTo 1 step 2) print(fizzBuzz(i))
    }
}
