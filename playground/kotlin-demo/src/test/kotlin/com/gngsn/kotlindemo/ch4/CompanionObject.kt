package com.gngsn.kotlindemo.ch4.CompanionObject

fun getFacebookName(accountId: Int) = "fb:$accountId"

class User {
    val nickname: String

    constructor(email: String) {
        nickname = email.substringBefore("@")
    }

    constructor(facebookAccountId: Int) {
        nickname = getFacebookName(facebookAccountId)
    }
}

//fun <T> loadFromJSON(factory: JSONFactory<T>): T {
//    return factory.fromJSON()
//}

//fun mainTest3() {
//    loadFromJSON(Person2)
//}

fun main() {
    val subscribingUser = User("bob@gmail.com")
    val facebookUser = User(4)

    println(subscribingUser.nickname)
    println(facebookUser.nickname)
}