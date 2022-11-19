package com.gngsn.ratelimit;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FixedWindowCounter implements RateLimiter {

    private final ConcurrentMap<Long, AtomicInteger> windows = new ConcurrentHashMap<>();
    private final long windowSizeInSec;  // window size (t1 - t0) sec
    private final long maxRequest; // max request per window size (sec)

    public FixedWindowCounter(int windowSizeInSec, int maxRequest) {
        this.windowSizeInSec = windowSizeInSec;
        this.maxRequest = maxRequest;
    }

    @Override
    public boolean allow() {
        long windowKey = (System.currentTimeMillis() / 1000) / windowSizeInSec;
        windows.putIfAbsent(windowKey, new AtomicInteger(0));

        return windows.get(windowKey).incrementAndGet() <= maxRequest;
    }
}