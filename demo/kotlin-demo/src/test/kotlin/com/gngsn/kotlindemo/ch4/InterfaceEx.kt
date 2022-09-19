package com.gngsn.kotlindemo.ch4

import org.junit.jupiter.api.Test


class InterfaceEx {
    @Test
    fun MultiImplButtonTest() {
        val button = MultiImplButton()
        button.showOff()
        button.click()
        button.setFocus(true)
    }

    @Test
    fun defineTest() {

    }
}

interface Clickable {
    fun click()                    // 일반 메소드 선언
    fun showOff() = println("I’m clickable!")    // 디폴트 구현이 있는 메소드
}

interface Focusable {
    fun setFocus(b: Boolean) = println("I ${if (b) "got" else "lost"} focus.")
    fun showOff() = println("I'm focusable!")
}

class Button : Clickable {
    override fun click() = println("I was clicked")
}

class MultiImplButton : Clickable, Focusable {
    override fun click() = println("Double implement Click")
    override fun showOff() {
        super<Clickable>.showOff()
        super<Focusable>.showOff()
    }
}