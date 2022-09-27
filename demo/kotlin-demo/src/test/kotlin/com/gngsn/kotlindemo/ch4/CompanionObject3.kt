package com.gngsn.kotlindemo.ch4.CompanionObject_Interface

import org.json.JSONObject
import org.json.JSONTokener

// 방법 1: 내부
class Person1(val name: String) {

    companion object Loader {
        fun fromJSON(jsonText: String): Person1 {
            val jsonObject = JSONTokener(jsonText).nextValue() as JSONObject
            return Person1(jsonObject.get("name") as String)
        }
    }
}

// 방법 2: 외부 Interface 상속
interface JSONFactory<T> {
    fun fromJSON(jsonText: String): T
}

class Person2(val name: String) {

    companion object : JSONFactory<Person2> {
        override fun fromJSON(jsonText: String): Person2 {
            val jsonObject = JSONTokener(jsonText).nextValue() as JSONObject
            return Person2(jsonObject.get("name") as String)
        }
    }
}


// 방법 3: 비즈니스 로직 모듈
class Person3(val name: String) {
    companion object {}
}

fun Person3.Companion.fromJSON(json: String): Person3 {
    val jsonObject = JSONTokener(json).nextValue() as JSONObject
    return Person3(jsonObject.get("name") as String)
}


fun main() {
    val json = "{name: gngsn}"

    val person1 = Person1.Loader.fromJSON(json)
    println(person1.name)

    val person2 = Person2.fromJSON(json)
    println(person2.name)

    val person3 = Person3.fromJSON(json)
    println(person3.name)

}