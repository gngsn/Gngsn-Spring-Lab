package com.gngsn.demo.cache;

import java.time.LocalDateTime;

public class CacheDetailDTO {

    private String cacheName;
    private Object key;
    private Object value;

    private long hitCount;
    private long missCount;

    private LocalDateTime createTime;

    public CacheDetailDTO() {
    }

    public CacheDetailDTO(final String cacheName, final Object key, final Object value, final long hitCount, final long missCount, final LocalDateTime createTime) {
        this.cacheName = cacheName;
        this.key = key;
        this.value = value;
        this.hitCount = hitCount;
        this.missCount = missCount;
        this.createTime = createTime;
    }

    public void setCacheName(final String cacheName) {
        this.cacheName = cacheName;
    }

    public void setKey(final Object key) {
        this.key = key;
    }

    public void setValue(final Object value) {
        this.value = value;
    }

    public void setHitCount(final long hitCount) {
        this.hitCount = hitCount;
    }

    public void setMissCount(final long missCount) {
        this.missCount = missCount;
    }

    public void setCreateTime(final LocalDateTime createTime) {
        this.createTime = createTime;
    }
}