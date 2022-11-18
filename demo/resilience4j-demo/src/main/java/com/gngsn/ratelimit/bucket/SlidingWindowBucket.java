package com.gngsn.ratelimit.bucket;

import com.gngsn.ratelimit.algorithm.SlidingWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SlidingWindowBucket {

    Logger log = LoggerFactory.getLogger(SlidingWindowBucket.class);
    Map<Integer, SlidingWindow> bucket;

    public SlidingWindowBucket(int id) {
        bucket = new HashMap<>();
        bucket.put(id, new SlidingWindow(1,5));
    }

    public void access(int id){
        if (bucket.get(id).allow()) {
            log.info(Thread.currentThread().getName() + " -> able to access the application");
        } else {
            log.info(Thread.currentThread().getName() + " -> Too many request, Please try after some time");
        }
    }
}
