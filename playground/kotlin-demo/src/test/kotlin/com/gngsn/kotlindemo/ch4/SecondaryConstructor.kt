package com.gngsn.kotlindemo.ch4

import org.junit.jupiter.api.Test

class SecondaryConstructor {
    open class View {
        constructor(size: Int) {}
        constructor(size: Int, color: String) {}
    }

    class MyButton : View {
        constructor(size: Int) : super(size) {}
        constructor(size: Int, color: String) : super(size, color) {}
    }

    class MyButton2 : View {
        constructor(size: Int) : this(size, "red") {}
        constructor(size: Int, color: String) : super(size, color) {
            // ...
        }
    }
}

class Interface {

    interface User {
        val nickname: String
    }

    class PrivateUser(override val nickname: String) : User

    class SubscribingUser(val email: String) : User {
        override val nickname: String
            get() = email.substringBefore('@')  // 매번 호출 때마다 substringBefore를 호출해 계산하는 커스텀 게터
    }

    class FacebookUser(val accountId: Int) : User {
        override val nickname = getFacebookName(accountId) // 객체 초기화 시 계산한 데이터를 뒷받침하는 필드에 저장했다가 불러오는 방식
        fun getFacebookName(_accountId: Int): String = "fb-${_accountId}"
    }

    //    >> val user = User("Alice")
//    user.address = "Elsenheimerstrasse 47, Address"
//    Address was changed for Alice: 80687 Muenchen"
//    "unspecified" -> "Elsenheimerstrasse 47, 80687 Muenchen"

    interface User2 {
        val email: String
        val nickname: String
            get() = email.substringBefore('@')
    }

    class LengthCounter {
        var counter: Int = 0
            private set // 이 클래스 밖에서 이 프로퍼티의 값을 바꿀 수 없다.

        fun addWord(word: String) {
            counter += word.length
        }
    }

    //    val lengthCounter = LengthCounter()
//    lengthcounter.addWord("Hi !")
//    println(lengthCounter.counter)
//    class Client(val name: String, val postalCode: Int)
    class Client(val name: String, val postalCode: Int) {
        override fun toString() = "Client(name=$name, postalCode=$postalCode)"
        override fun equals(other: Any?): Boolean { // Any는 java.lang.object에 대응하는 클래스로, 코롤린의 모든 클래스의 최상위 클래스다. "Any?"는 널이 될 수 있는 타입이므로 "other"는 null일 수 있다.
            if (other == null || other !is Client)  // "other"가 Client인지 검사한다.
                return false
            return name == other.name       // 두 객체의 프로퍼티 값이 서로 같은지 검사한다
                    && postalCode == other.postalCode
        }

        override fun hashCode(): Int = name.hashCode() * 31 + postalCode
    }

    @Test
    fun main() {
        val client1 = Client("gngsn", 10580)
        println(client1) // Client(name = "gngsn", postalCode = 10580)

        val processed = hashSetOf(Client("gngsn", 10580))
        println(processed.contains(Client("gngsn", 10580))) // false

        val client2 = Client("gngsn", 10580)
        println(client2) // Client(name = "gngsn", postalCode = 10580)
        println(client1 == client2)  // false
    }


    class Client3(val name: String, val postalCode: Int) {
        override fun hashCode(): Int = name.hashCode() * 31 + postalCode
    }

}

data class Client(val name: String, val postalCode: Int)