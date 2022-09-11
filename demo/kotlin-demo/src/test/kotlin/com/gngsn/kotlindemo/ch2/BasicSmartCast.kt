package com.gngsn.kotlindemo.ch2;

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

interface Expr
class Num(val value: Int) : Expr  // value 라는 프로퍼티만 존재하는 단순한 클래스로 Expr 인터페이스를 구현한다.
class Sum(val left: Expr, val right: Expr) : Expr
// Expr 타입의 객체라면 어떤 것이나 Sum 연산의 인자가 될 수 있으므로 Num이나 다른 Sum이 인자로 올 수 있다.
fun evalWhen(e: Expr): Int = when (e) {
    is Num -> e.value
    is Sum -> eval(e.right) + eval(e.left)
    else -> throw IllegalArgumentException("Unknown expression")
}

fun evalWithLogging(e: Expr): Int =
    when (e) {
        is Num -> {
            println("num: ${e.value}")
            e.value                      //이 식이 블록의 마지막 식이므로 e의 타입이 Num이면 e.value가 반환된다.
        }
        is Sum -> {
            val left = evalWithLogging(e.left)
            val right = evalWithLogging(e.right)
            println ("sum: $left+$right")
            left + right                    //<-------- e의 타입이 Sum이면 이 식의 값이 반환된다.
        }
        else -> throw IllegalArgumentException("Unknown expression")
    }

fun eval(e: Expr): Int {
    if (e is Num) {
        val n = e as Num
        return n.value
    }
    if (e is Sum) {   // 변수 e에 대해 스마트 캐스트를 사용
        return eval(e.right) + eval(e.left)
    }
    throw IllegalArgumentException("Unknown expression")
}

@Test
fun test_sum() {
    val result = eval(Sum(Sum(Num(1), Num(2)), Num(4)))
    Assertions.assertEquals(7, result)
    evalWithLogging(Sum(Sum(Num(1), Num(2)), Num(4)))
}
class Basic_smartCast {


}
