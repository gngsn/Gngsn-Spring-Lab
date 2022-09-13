package com.gngsn.kotlindemo.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class versusAsync {
    fun log(msg: String) = println("${now()}:${Thread.currentThread()}:${msg}")

    @Test
    fun sumAll() {
        runBlocking {
            val dl = async { delay(1000L); 1 }
            log("after async(dl)")
            val d2 = async { delay(2000L); 2 }
            log(" after async(d2)")
            val d3 = async { delay(3000L); 3 }
            log("after async(d3)")

            log("1+2+3 = ${dl.await() + d2.await() + d3.await()}")
            log ("after await all & add")
        }

    } // 5sec 136ms

}
