package com.gngsn.ratelimit.algorithm;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FixedWindow {
    private final ConcurrentMap<Long, AtomicInteger> windows = new ConcurrentHashMap<>();
    private final long windowSize;  // window size (t1 - t0) ns
    private final long maxRequest; // max request per window size (ns)

    public FixedWindow(int windowSize, int maxRequest) {
        this.windowSize = windowSize;
        this.maxRequest = maxRequest;
    }

    public synchronized boolean allow() {
        long windowKey = System.nanoTime() / windowSize;
        windows.putIfAbsent(windowKey, new AtomicInteger(0));

        System.out.println("windowSize: " + windowKey + ", maxRequest: " + maxRequest);
        return windows.get(windowKey).incrementAndGet() <= maxRequest;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("");
        for(Map.Entry<Long, AtomicInteger> entry:  windows.entrySet()) {
            sb.append(entry.getKey());
            sb.append(" --> ");
            sb.append(entry.getValue());
            sb.append("\n");
        }
        return sb.toString();
    }
}