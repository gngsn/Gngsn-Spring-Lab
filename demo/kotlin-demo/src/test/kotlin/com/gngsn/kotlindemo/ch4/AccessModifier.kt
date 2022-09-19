package com.gngsn.kotlindemo.ch4

open class RichButton : Clickable {  // extends O
    fun disable() {}                 // final -> override X
    open fun animate() {}            // override O
    override fun click()             // override O, 오버라이드한 메소드는 기본적으로 열려있다.
            = println("Double implement Click")
}

abstract class Animated {           // 인스턴스 생성 X
    val ds: String = ""
    abstract fun animate()          // 추상 함수. 구현 X. 오버라이드 필수
    open fun stopAnimating() {}     // override O
    fun animateTwice() {}           // 기본적으로 final -> override X
}