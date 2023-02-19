package com.gngsn.kotlindemo.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.sqrt


class Basic {
    fun now() = ZonedDateTime.now().toLocalTime().truncatedTo(ChronoUnit.MILLIS)

    fun log(msg: String) = println("${now()}:${Thread.currentThread()}:${msg}")

    fun launchInGlobalScope() {
        GlobalScope.launch {    // main과 GlobalScope.launch가 만들어낸 코루틴이 서로 다른 스레 드에서 실행된다는 점이
            log("coroutine started.")
        }
    }

    fun launchInRunBlocking() = runBlocking {
        launch { log("GlobalScope.launch started.") }
    }

    @Test
    fun launchTest() {
        log("main () started.")
        launchInGlobalScope()
        log("launchlnGlobalScope() executed")
        Thread.sleep(5000L)
        log("main() terminated")
    }
    /*
        14:41:29.079:Thread[main,5,main]:main () started.
        14:41:29.267:Thread[main,5,main]:launchlnGlobalScope() executed
        14:41:29.276:Thread[DefaultDispatcher-worker-2,5,main]:coroutine started.
        14:41:34.272:Thread[main,5,main]:main() terminated
    */

    @Test
    fun runBlockingLaunchTest() {
        log("main () started.")
        launchInRunBlocking()
        log("launchlnGlobalScope() executed")
        Thread.sleep(5000L)
        log("main() terminated")
    }
    /*
        16:30:42.602:Thread[main,5,main]:main () started.
        16:30:42.724:Thread[main @coroutine#2,5,main]:GlobalScope.launch started.
        16:30:42.726:Thread[main,5,main]:launchlnGlobalScope() executed
        16:30:47.726:Thread[main,5,main]:main() terminated
    */

    @Test
    fun yieldExample() = runBlocking {
        launch {
            log("1")
            yield()
            log("3")
            yield()
            log("5")
        }
        log("after first launch")
        launch {
            log("2")
            delay(1000L)
            log("4")
            delay(1000L)
            log("6")
        }
        log("after second launch")
    }

    // ================ CoroutineScope Example: Scope 종료를 delay 대기하는 경우 ================
    private suspend fun MutableList<Int>.sum(): Int {
        val ret = this.sumBy { it }
        delay(100) // 임의로 delay 적용
        return ret
    }

    @Test
    fun test() {
        print("run ")
        CoroutineScope(Dispatchers.Default).launch { //launch {}의 return job을 통해 동작 지정 가능
            (0..10).forEach {
                val sum = (it..10).toMutableList().sum()
                print("$sum ")
            }
        }

        print("wait ")
        runBlocking {
            delay(500L)         // 시간으로는 정확하지 않다 (-> join이랑 비교하기 위한 장치)
        }
        print("Test end ")
    }  // [637ms] run wait 55 55 54 52 Test end


    @Test
    fun test_nowait() { // launch의 return인 Job을 활용하면 예측 불가능한 시간으로 종료를 기다릴 필요가 없어진다.
        print("run ")
        val job = CoroutineScope(Dispatchers.Default).launch { //launch {}의 return job을 통해 동작 지정 가능
            (0..10).forEach {
                val sum = (it..10).toMutableList().sum()
                print("$sum ")
            }
        }
        print("wait ")
        runBlocking {
            job.join()         // join() : scope 동작이 끝날 때까지 대기하며, CoroutinScope 안에서 호출 가능
        }
        print("Test end ")
    } // [1sec 267ms] run wait 55 55 54 52 49 45 40 34 27 19 10 Test end


    // ================ GlobalScope example ================
    fun CoroutineScope.produceNumbers() = produce<Int> {
        var x = 1
        while (true) send(x++) // infinite stream of integers starting from 1
    }

    fun ReceiveChannel<Int>.sqrt(): ReceiveChannel<Double> =  // 이거 어떻게 실행시키지?
        GlobalScope.produce(Dispatchers.Unconfined) {
            for (number in this@sqrt) {
                send(Math.sqrt(number.toDouble()))
            }
        }

    @Test
    fun channelTest() = runBlocking {
        val channel = Channel<Int>()
        launch {
            // this might be heavy CPU-consuming computation or async logic, we'll just send five squares
            for (x in 1..5) channel.send(x + x)
        }  // here we print five received integers:
        repeat(5) { print("${channel.receive()} ") }
        println("Done!")
    } // 2 4 6 8 10 Done!

    // =============== Dispatcher ===============
    @Test
    fun dispatcherTestClient() {
        val job = dispatcherTest()
    }

    fun dispatcherTest() = runBlocking {
        launch { // context of the parent, main runBlocking coroutine
            println("main runBlocking : I'm working in thread '${Thread.currentThread().name}'")
        } //지정하지 않았기에 외부 currentThread에 따른다.
        launch(Dispatchers.Unconfined) { // 특정 스레드에 종속되지 않음 ? 메인 스레드 사용
            println("Unconfined : I’m working in thread '${Thread.currentThread().name}'")
        }
        launch(Dispatchers.Default) { // will get dispatched to DefaultDispatcher
            println("Default : I'm working in thread '${Thread.currentThread().name}'")
        }    // Work thread에서 동작

        launch(newSingleThreadContext("MyOwnThread")) { // will get its own new thread
            println("newSingleThreadContext: I'm working in thread '${Thread.currentThread().name}'")
        }
    }
    /*
        Default : I'm working in thread 'DefaultDispatcher-worker-1 @coroutine#3'
        newSingleThreadContext: I'm working in thread 'MyOwnThread @coroutine#4'
        main runBlocking : I'm working in thread 'main @coroutine#2'
    */

    // ========= suspend =========

    private suspend fun waitOne(): Int {
        delay(100L)
        return 100 // 100 ms delay 후 100을 리턴

    }

    private suspend fun waitTwo(): Int {
        delay(200L)
        return 200
    } // 200 ms delay 후 200을 리턴

    @Test // 위 2개의 결과를 async/await을 이용하여 결과를 출력
    fun testTwo() = runBlocking {
        val one = CoroutineScope(Dispatchers.Default).async { waitOne() }
        val two = CoroutineScope(Dispatchers.Default).async { waitTwo() }

        println("wait ${one.await()} ${two.await()}")
    } // [335ms] wait 100 200


}

