package com.gngsn.demo.cache;

import lombok.Data;

@Data
public class CacheDetailDTO {

    private String cacheName;
    private Object key;
    private Object value;

    private long hitCount;
    private long missCount;

    private long loadSuccessCount;
    private long loadFailureCount;
    private long totalLoadTime;
    private long evictionCount;
    private long evictionWeight;
}