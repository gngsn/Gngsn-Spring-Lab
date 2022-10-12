package com.gngsn.kotlindemo.ch5

class LambdaCaptured {
}

fun capturedRef() {
    class Ref<T>(var value: T)    // 변수를 변경 가능하도록 포획하기 위한 클래스

    val counter = Ref(0)
    val inc = { counter.value++ }
}

fun capturedDirect() {
    var counter = 0
    val inc = { counter++ }
}