package com.gngsn.kotlindemo.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit


class Basic {
    fun now() = ZonedDateTime.now().toLocalTime().truncatedTo(ChronoUnit.MILLIS)

    fun log(msg: String) = println("${now()}:${Thread.currentThread()}:${msg}")

    fun launchInGlobalScope() {
        GlobalScope.launch {    // main과 GlobalScope.launch가 만들어낸 코루틴이 서로 다른 스레 드에서 실행된다는 점이
            log("coroutine started.")
        }
    }

    @Test
    fun test_main() {
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
    fun ReceiveChannel<Int>.sqrt(): ReceiveChannel<Double> =
        GlobalScope.produce(Dispatchers.Unconfined) {
            for (number in this@sqrt) {
                send(Math.sqrt(number.toDouble()))
            }
        }
}