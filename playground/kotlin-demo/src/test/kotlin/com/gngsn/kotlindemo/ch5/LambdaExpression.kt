package com.gngsn.kotlindemo.ch5.LambdaExpression

data class Person(val name: String, val age: Int)

val people = listOf(Person("Alice", 29), Person("Bob", 31))


class LambdaExpression {

    fun step1() {
        // lambda example: 정식적 람다 정의
        people.maxByOrNull({ p: Person -> p.age })
    }

    fun step2() {
        // 코틀린에는 함수 호출 시 맨 뒤에 있는 인자가 람다 식이라면 그 람다를 괄호 밖으로 빼낼 수 있다.
        people.maxByOrNull() { p: Person -> p.age }
    }

    fun step3() {
        // 람다가 어떤 함수의 유일한 인자이고 괄호 뒤에 빈 괄호를 없애도 된다.
        people.maxByOrNull { p: Person -> p.age }
    }

    fun step4() {
        // 파라미터 타입을 생략(컴파일러가 추론)
        people.maxByOrNull { p -> p.age }
    }

    fun step5() {
        // 람다의 파라미터가 하나뿐이고 그 타입을 컴파일러가 추론할 수 있는 경우 it을 바로 쓸 수 있다.
        people.maxByOrNull { it.age }             // "it"은 자동 생성된 파라미터 이름이다.
    }

    fun step6() {
        // 객체로 넘기기
        val getAge = { p: Person -> p.age }
        people.maxByOrNull(getAge)
    }
}

val sum = { x: Int, y: Int -> x + y }
fun main() {

    // lambda example 2
    val names1 = people.joinToString(separator = " ", transform = { p: Person -> p.name })
    val names2 = people.joinToString(" ") { p: Person -> p.name }

    // lambda example 3 : 본문이 여러 줄로 이뤄진 경우 본문의 맨 마지막에 있는 식이 람다의 결과 값이 된다
    val sum = { x: Int, y: Int ->
        {
            println("Computing the sum of $x and $y...")
            x + y // return 값
        }
    }
}

