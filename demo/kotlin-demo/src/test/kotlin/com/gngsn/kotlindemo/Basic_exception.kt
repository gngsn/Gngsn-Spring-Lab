package com.gngsn.kotlindemo;

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader

class Basic_exception {

    private fun readNumber(reader:BufferedReader): Int? {  // throws로 던질 수 있는 예외 명시 X
        try {
            val line = reader.readLine()
            return Integer.parseInt(line)
        }
        catch (e: NumberFormatException) {
            return null
        }
        finally {
            reader.close()
        }
    }

    private fun readNumber2(reader:BufferedReader): Int? {
        return try {
            Integer.parseInt(reader.readLine())  // return 값
        } catch (e: NumberFormatException) {
            null
        } finally {
            reader.close()
        }
    }
    @Test
    fun readerTest() {
        val reader = BufferedReader(StringReader("239"))
        Assertions.assertEquals(239, readNumber(reader))

        val errorReader = BufferedReader(StringReader("239-"))
        Assertions.assertEquals(null, readNumber(errorReader))
//        Assertions.assertThrows(Class<NumberFormatException>, fun() = readNumber(reader))
    }


}
