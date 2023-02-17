package com.gngsn.kotlindemo.ch5.LambdaLocalVariable

class LambdaLocalVariable {
}

fun printMessagesWithPrefix(messages: Collection<String>, prefix: String) {
    messages.forEach { println("$prefix $it") } //각 원소에 대해 수행할 작업을 람다로 받는다.람다 안에서 함수의 파라미터를 사용한다.
}

fun main() {
    val errors = listOf("403 Forbidden", "404 Not Found")
    printMessagesWithPrefix(errors, "Error: ")

    val responses = listOf("200 OK", "418 I’m a teapot", "500 Internal Server Error")
    printProblemCounts(responses)
}


fun printProblemCounts(responses: Collection<String>) {

    var clientErrors = 0    // 람다에서 사용할 변수를 정의한다.
    var serverErrors = 0
    responses.forEach {
        if (it.startsWith("4")) {
            clientErrors++ // 1
        } else if (it.startsWith("5")) {
            serverErrors++ // 람다 안에서 람다 밖의 변수를 변경한다.
        }
    }
    println("$clientErrors client errors, $serverErrors server errors")
}

data class Person(val name: String, val age: Int)