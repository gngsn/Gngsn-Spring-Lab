package com.gngsn.ratelimit.app;

import com.gngsn.ratelimit.SlidingWindowBucket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SlidingWindowBucketApp {

    public static void main(String[] args) throws InterruptedException {
        SlidingWindowBucket slidingWindowBucket = new SlidingWindowBucket(1);
        ExecutorService executors = Executors.newFixedThreadPool(12);

        for (int i = 0; i < 12; i++) {
            executors.execute(() -> slidingWindowBucket.access(1));
            Thread.sleep(100);
        }

        executors.shutdown();
    }
}
