package com.gngsn.kotlindemo.ch3

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


// ======== 확장 함수 ========
fun <T> Collection<T>.joinToString(  //<----- Collection〈T〉 에 대한 확장 함수를 선언한다.
    separator: String = ", ",  //         파라미터의 디폴트 값을 지정한다.
    prefix: String = "",
    postfix: String = ""
): String {
    val result = StringBuilder(prefix)
    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

fun Collection<String>.join(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
) = joinToString(separator, prefix, postfix)

open class View {
    open fun click() = println("View clicked")
}

class Button : View() {  //Button은 View를 확장한다.
    override fun click() = println("Button clicked")
}

fun View.showOff() = println("I’m a view! ")
fun Button.showOff() = println("I ’m a button! ")

val String.lastChar: Char get() = get(length - 1)
var StringBuilder.lastChar: Char
    get() = get(length - 1)
    set(value: Char) = this.setCharAt(length - 1, value)


class FunExtension {

    @Test
    fun fun_extension() {
        val list = listOf<Number>(1, 2, 3)
        Assertions.assertEquals("1, 2, 3", list.joinToString())
        Assertions.assertEquals("# 1, 2, 3;", list.joinToString(", ", postfix = ";", prefix = "# "))

        Assertions.assertEquals("one two eight", listOf("one", "two", "eight").join(" "))
        // listOf(1, 2, 8).join() // Unresolved reference
    }

    @Test
    fun fun_extension_overload() {
        val view: View = Button()
        view.click()        // override function 호출하기 때문에 Button method 호출
        view.showOff()      // 확장 함수는 override 되지 않기 때문에 View method 호출
    }

    @Test
    fun fun_extension_properties() {
        println("Kotlin".lastChar)  // n

        val sb = StringBuilder("Kotlin?")
        sb.lastChar = '!'
        println(sb.lastChar)        // !
        // 자바에서 확장 프로퍼티를 사용하고 싶다면 항상 StringUtilKt.getLastChar("Java") 처럼 게터나 세터를 명시적으로 호출해야 한다.
    }
}