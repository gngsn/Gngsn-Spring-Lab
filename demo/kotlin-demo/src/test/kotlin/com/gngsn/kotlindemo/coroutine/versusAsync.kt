package com.gngsn.kotlindemo.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class versusAsync {
    fun now() = ZonedDateTime.now().toLocalTime().truncatedTo(ChronoUnit.MILLIS)
    fun log(msg: String) = println("${now()}:${Thread.currentThread()}:${msg}")

    @Test
    fun sumAll() {
        runBlocking {
            val d1 = async { delay(1000L); 1 }
            log("after async(dl)")
            val d2 = async { delay(2000L); 2 }
            log("after async(d2)")
            val d3 = async { delay(3000L); 3 }
            log("after async(d3)")

            log("1+2+3 = ${d1.await() + d2.await() + d3.await()}")
            log("after await all & add")
        }

    } // 5sec 136ms
    /*
        22:45:25.208:Thread[main @coroutine#1,5,main]:after async(d1)
        22:45:25.232:Thread[main @coroutine#1,5,main]:after async(d2)
        22:45:25.232:Thread[main @coroutine#1,5,main]:after async(d3)
        22:45:28.247:Thread[main @coroutine#1,5,main]:1+2+3 = 6
        22:45:28.247:Thread[main @coroutine#1,5,main]:after await all & add
    */

    @Test
    fun sumAllLaunch() = run {
        var d1 = 0
        var d2 = 0
        var d3 = 0

        runBlocking {
            launch { delay(1000L); d1 = 1 }
            log("after launch(dl)")
            launch { delay(2000L); d2 = 2 }
            log("after launch(d2)")
            launch { delay(3000L); d3 = 3 }
            log("after launch(d3)")
        }

        log("1+2+3 = ${d1 + d2 + d3}")
        log("after await all & add")
    } // 3sec 146ms
    /*
        22:45:28.263:Thread[main @coroutine#5,5,main]:after launch(dl)
        22:45:28.264:Thread[main @coroutine#5,5,main]:after launch(d2)
        22:45:28.265:Thread[main @coroutine#5,5,main]:after launch(d3)
        22:45:31.267:Thread[main,5,main]:1+2+3 = 6
        22:45:31.267:Thread[main,5,main]:after await all & add
    */

}

