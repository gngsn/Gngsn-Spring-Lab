package com.gngsn.kotlindemo.ch4


class ClassEx {
    fun defineTest() = println("default access modifier test!")


}

class User constructor(_nickname: String) { //primary constructor

    val nickname: String

    init {  // initial block
        nickname = _nickname
    }
}

class UserV2(_nickname: String) {
    val nickname = _nickname
}

class UserV3(val nickname: String)

class UserV4(val nickname: String, val isSubscribed: Boolean = true)

val hyun = User("현석 ")

class ExtendClass {
    open class User(val nickname: String) { /* ... */ }
    class TwitterUser(nickname: String) : User(nickname) { /* ... */ }

// 클래스를 정의할 때 별도로 생성자를 정의하지 않으면 컴파일러가 자동으로 아무 일도 하지 않는 인자가 없는 디폴트 생성자를 만들어준다.

    open class Button
    class RadioButton : Button()
}

class Secretive private constructor() {}