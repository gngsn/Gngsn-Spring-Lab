package com.gngsn.kotlindemo.ch4

import java.io.Serializable

class InnerClass {}

interface State : Serializable

interface View {
    fun getCurrentState(): State
    fun restoreState(state: State)
}

class Button2 : View {
    override fun getCurrentState(): State = ButtonState()
    override fun restoreState(state: State) {
        /*...*/
    }

    // 이 클래스는 자바의 정적 중첩 클래스와 대응한다.
    class ButtonState : State {
        /*...*/
    }
}

class Outer {

    inner class Inner {
        fun getOuterReference(): Outer = this@Outer
    }

    class Nested {
        fun getOuterReference(): Outer = Outer()
    }
}