package com.gngsn.ratelimit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SlidingWindowBucket {

    Logger log = LoggerFactory.getLogger(SlidingWindowBucket.class);
    Map<Integer, SlidingWindowCounter> bucket;

    public SlidingWindowBucket(int id) {
        bucket = new HashMap<>();
        bucket.put(id, new SlidingWindowCounter(1, 1, 5));
    }

    public void access(int id) {
        if (bucket.get(id).allow()) {
            log.info(Thread.currentThread().getName() + " -> able to access the application");
        } else {
            log.info(Thread.currentThread().getName() + " -> Too many request, Please try after some time");
        }
    }
}
