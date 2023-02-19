package com.gngsn.demo.cache;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CacheStatsDTO {

    private long hitCount;
    private long missCount;
    //    private long loadSuccessCount;
//    private long loadFailureCount;
//    private long totalLoadTime;
    private long evictionCount;
    private long evictionWeight;
}