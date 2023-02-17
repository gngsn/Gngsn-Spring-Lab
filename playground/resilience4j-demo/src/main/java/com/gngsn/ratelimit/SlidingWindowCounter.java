package com.gngsn.ratelimit;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SlidingWindowCounter {

    private final ConcurrentMap<Long, AtomicInteger> windows = new ConcurrentHashMap<>();
    private final long windowSizeInSec;
    private final long maxRequest;
    private final long interval;

    public SlidingWindowCounter(long windowSizeInSec, long interval, long maxRequest) {
        this.windowSizeInSec = windowSizeInSec;
        this.interval = interval;
        this.maxRequest = maxRequest;
    }

    public boolean allow() {
        long now = System.currentTimeMillis() / 1000;
        long curWindowKey = now / windowSizeInSec;

        windows.putIfAbsent(curWindowKey, new AtomicInteger(0));
        long preWindowKey = curWindowKey - interval;
        AtomicInteger preCount = windows.get(preWindowKey);

        if (preCount == null) {
            return windows.get(curWindowKey).incrementAndGet() <= maxRequest;
        }

        double preWeight = 1 - (now - curWindowKey) / interval;
        long count = (long) (preCount.get() * preWeight + windows.get(curWindowKey).incrementAndGet());
        return count <= maxRequest;
    }
}