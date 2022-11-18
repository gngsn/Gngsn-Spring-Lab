package com.gngsn.ratelimit.algorithm;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SlidingWindow {

    private final ConcurrentMap<Long, AtomicInteger> windows = new ConcurrentHashMap<>();
    private final long windowSize;
    private final long maxRequest;
    private final long interval;

    public SlidingWindow(long windowSize, long interval, long maxRequest) {
        this.windowSize = windowSize;
        this.interval = interval;
        this.maxRequest = maxRequest;
    }

    boolean allow() { // 30sec 5 times -> 30sec
        long now = System.nanoTime();
        long curWindowKey = now / windowSize;

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