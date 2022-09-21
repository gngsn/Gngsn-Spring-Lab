package com.gngsn.kotlindemo.ch4

// 추상 클래스
abstract class Animated {           // 인스턴스 생성 X
    val duration: Long = 0
    abstract fun animate()          // 추상 함수. 구현 X. 오버라이드 필수
    open fun stopAnimating() {}     // override O
    fun animateTwice() {}           // 기본적으로 final -> override X
}


// 접근 제한자
open class RichButton : Clickable {  // extends O
    fun disable() {}                 // final -> override X
    open fun animate() {}            // override O
    override fun click()             // override O, 오버라이드한 메소드는 기본적으로 열려있다.
            = println("Double implement Click")
}

/*
    public: 모든 곳에서 볼 수 있다.
    internal: 같은 모듈 내에서만 볼 수 있다.
    protected: 하위 클래스 내에서만 볼 수 있다. (최상위 선언 불가)
    private: 같은 클래스 내에서만 볼 수 있다. (최상위 선언 시, 같은 파일 내)
*/
internal open class TalkativeButton : Focusable {
    private fun yell() = println("Hey!")
    protected fun whisper() = println("Let's talk! ")
}

//fun TalkativeButton.giveSpeech() {  // 'public' member exposes its 'internal' receiver type TalkativeButton 오류: "public" 멤버가 자신의 "internal" 수신 타입인 TalkativeButton 을 노출함
//    private fun yell()                          // 오류: "yell"에 접근할 수 없음: "yell"은 "TalkativeButton"의 "private" 멤버임
//    whisper()                       // 오류： "whisper"에 접근할 수 없음: "whisper"는 "TalkativeButton"의 "protected" 멤버임
//}