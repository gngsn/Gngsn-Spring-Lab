package com.gngsn.ratelimit.app;

import com.gngsn.ratelimit.FixedWindowCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedWindowCounterApp {

    private static final Logger log = LoggerFactory.getLogger(FixedWindowCounterApp.class);
    private static final int THREAD_COUNT = 24;

    public static void main(String[] args) throws InterruptedException {

        FixedWindowCounter fixedWindowCounter = new FixedWindowCounter(1 /* sec */, 10);
        ExecutorService executors = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT / 2; i++) {
            executors.execute(() -> accessLog(fixedWindowCounter.allow()));
        }

        Thread.sleep(1_000);
        log.info("======= window key change -> refill request count ======\n");

        for (int i = 0; i < THREAD_COUNT / 2; i++) {
            executors.execute(() -> accessLog(fixedWindowCounter.allow()));
        }

        executors.shutdown();
    }


    static void accessLog(boolean allow) {
        if (allow) {
            log.info(Thread.currentThread().getName() + " -> able to access the application");
        } else {
            log.info(Thread.currentThread().getName() + " -> Too many request, Please try after some time");
        }
    }
}
