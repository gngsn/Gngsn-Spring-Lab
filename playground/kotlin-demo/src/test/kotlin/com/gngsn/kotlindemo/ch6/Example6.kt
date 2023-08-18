package com.gngsn.kotlindemo.ch6.Example6

class People(
    val firstName: String,
    val lastName: String) {

    override fun equals(o: Any?): Boolean {
        val otherPerson = o as? People ?: return false
        return otherPerson.firstName == firstName && otherPerson.lastName == lastName
    } // 타입이 서로 일치하지  않으면 falsel 반환한다. 안전한 캐스트를 하고 나면 otherPerson 이 Person 타입으로 스마트 캐스트된다.
    
    override fun hashCode(): Int
        = firstName.hashCode() * 37 + lastName.hashCode()
}