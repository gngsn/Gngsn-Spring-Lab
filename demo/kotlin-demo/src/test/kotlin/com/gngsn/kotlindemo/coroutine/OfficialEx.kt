package com.gngsn.kotlindemo.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test

class OfficialEx {

    @Test
    fun coroutineEx() = runBlocking { // this: CoroutineScope
        launch { // launch a new coroutine and continue
            delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
            println("World!") // print after delay
        }
        println("Hello") // main coroutine continues while a previous one is delayed
    }

    // ====== refactoring 1: suspend function ======

    @Test
    fun suspendEx() = runBlocking { // this: CoroutineScope
        launch { doWorld() }
        println("Hello")
    }

    suspend fun doWorld() {
        delay(1000L)
        println("World!")
    }

    // ====== refactoring 2: coroutineScope suspend function ======

    @Test
    fun coroutineScopeEx() = runBlocking {
        suspendDoWorld()
    }

    suspend fun suspendDoWorld() = coroutineScope {  // this: CoroutineScope
        launch {
            delay(1000L)
            println("World!")
        }
        println("Hello")
    }

    // ====== refactoring 3: coroutineScope suspend function ======

    @Test // Sequentially executes doWorld followed by "Done"
    fun explicitJobEx() = runBlocking {
        explicitJobDoWorld()
        println("Done")
    }

    // Concurrently executes both sections
    suspend fun explicitJobDoWorld() = coroutineScope { // this: CoroutineScope
        launch {
            delay(2000L)
            println("World 2")
        }
        launch {
            delay(1000L)
            println("World 1")
        }
        println("Hello")
    }

    // === coroutine memory managing


}
