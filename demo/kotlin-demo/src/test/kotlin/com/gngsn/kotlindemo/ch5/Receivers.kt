package com.gngsn.kotlindemo.ch5

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Receivers {

    fun alphabet(): String {
        val result = StringBuilder()
        for (letter in 'A'..'Z') {
            result.append(letter)
        }
        result.append("\nNow I know the alphabet!")
        return result.toString()
    }

    fun alphabetRefac(): String {
        val stringBuilder = StringBuilder()
        return with(stringBuilder) {
            for (letter in 'A'..'Z') {
                append(letter)
            }
            append("\nNow I know the alphabet!")
            this.toString()
        }
    }

    fun alphabetRefac2() = with(StringBuilder()) {
        for (letter in 'A'..'Z') {
            append(letter)
        }

        append("\nNow I know the alphabet!")
        toString()
    }


    @Test
    fun withTest() {
        Assertions.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ\nNow I know the alphabet!", alphabet())
        Assertions.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ\nNow I know the alphabet!", alphabetRefac())
    }


    fun alphabetRefacApply() = StringBuilder().apply {
        for (letter in 'A'..'Z') {
            append(letter)
        }
        append("\nNow I know the alphabet!")
    }.toString()

    fun alphabetBuildString() = buildString {
        for (letter in 'A'..'Z') {
            append(letter)
        }
        append("\nNow I know the alphabet!")
    }

//    fun createViewWithCustomAttributes(context: Context) =
//        Textview(context).apply {
//            text = "Sample Text",
//            textsize = 20.0
//            setPadding(10, 0, 0, 0)
//        }

    @Test
    fun applyTest() {
        Assertions.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ\nNow I know the alphabet!", alphabetRefacApply())
    }
}
