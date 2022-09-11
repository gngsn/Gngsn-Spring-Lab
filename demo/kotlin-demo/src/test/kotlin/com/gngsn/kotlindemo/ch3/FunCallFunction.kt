package com.gngsn.kotlindemo.ch3

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FunCallFunction {

    fun <T> joinToString(
        collection: Collection<T>,
        sep: String,
        prefix: String,
        postfix: String
    ): String {
        val result = StringBuilder(prefix)

        for ((index, element) in collection.withIndex()) {
            if (index > 0) result.append(sep)
            result.append(element)
        }

        result.append(postfix)
        return result.toString()
    }

    @Test
    fun ex_fun() {
        val list = listOf<Number>(1, 2, 3)
        Assertions.assertEquals("(1; 2; 3)", joinToString(list, "; ", "(", ")"))

        // named parameter 인자 명을 쓸 수 있음
        Assertions.assertEquals(" 1 2 3.", joinToString(list, sep = " ", prefix = " ", postfix = "."))
    }

    @JvmOverloads
    fun <T> joinToString_usingDefault(
        collection: Collection<T>,
        sep: String = " ",
        prefix: String = "",
        postfix: String = ""
    ): String {
        val result = StringBuilder(prefix)

        for ((index, element) in collection.withIndex()) {
            if (index > 0) result.append(sep)
            result.append(element)
        }

        result.append(postfix)
        return result.toString()
    }

    @Test
    fun ex_fun_default() {
        val list = listOf<Number>(1, 2, 3)
        Assertions.assertEquals("1 2 3", joinToString_usingDefault(list))
        Assertions.assertEquals("1 2 3", joinToString_usingDefault(list, " "))
        Assertions.assertEquals("1 2 3", joinToString_usingDefault(list, " ", ""))
        Assertions.assertEquals("1 2 3", joinToString_usingDefault(list, " ", "", ""))

        joinToString(list, ", ", postfix = "; ", prefix = "#") // # l, 2, 3;
    }


    fun performOperation() {
        opCount++
        // ...
    }

    fun reportOperationCount() {
        println("Operation performed $opCount times")
    }
}

// ==== 최상위 프로퍼티 ====
var opCount = 0
val UNIX_LINE_SEPARATOR = "\n"
const val CONST_UNIX_LINE_SEPARATOR = "\n"
// public static final String UNIX_LINE_SEPARATOR = "\n";