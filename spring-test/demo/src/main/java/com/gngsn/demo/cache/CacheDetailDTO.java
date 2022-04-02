package com.gngsn.demo.cache;

import lombok.Data;

@Data
public class CacheDetailDTO {

    private String cacheName;
    private Object key;
    private Object value;
    private long version;
    private long hitCount;
    private long missCount;
    private String creationTime;
    private String lastAccessTime;
}