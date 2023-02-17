package com.gngsn.kotlindemo.ch4.AnonymousObject

import java.awt.Window
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

val window = Window(null)

fun main() {

    window.addMouseListener(
        object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) { // 무명 객체를 선언한다.
                // ...
                // ...MouseAdapter# 확장하는 MouseAdapter의 오버라이드한다.메소드를
            }

            override fun mouseEntered(e: MouseEvent) {
                // ...
            }
        }
    )

}

fun countclicks(window: Window) {
    var clickCount = 0

    //  로컬 변수를 정의한다.
    window.addMouseListener(object : MouseAdapter() {

        override fun mouseClicked(e: MouseEvent) {
            clickCount++ // 로컬 변수의 값을 변경한다.
        }
    })
    // ...
}

