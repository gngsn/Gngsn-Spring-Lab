package com.gngsn.ratelimit.app;

import com.gngsn.ratelimit.TokenBucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TokenBucketApp {

    private static final Logger log = LoggerFactory.getLogger(TokenBucketApp.class);
    private static final int THREAD_COUNT = 12;

    public static void main(String[] args) throws InterruptedException {

        TokenBucket tokenBucket = new TokenBucket(3, 3); // 3 requests per 3 seconds
        ExecutorService executors = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executors.execute(() -> accessLog(tokenBucket.allow()));
            Thread.sleep(100);
        }

        executors.shutdown();
    }
    static void accessLog(boolean allow) {
        if (allow) {
            log.info(Thread.currentThread().getName() + " -> {}: able to access the application", System.currentTimeMillis() / 1000);
        } else {
            log.info(Thread.currentThread().getName() + " -> {}: Too many request, Please try after some time", System.currentTimeMillis() / 1000);
        }
    }
}
