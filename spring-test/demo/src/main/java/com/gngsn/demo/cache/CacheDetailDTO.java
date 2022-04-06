package com.gngsn.demo.cache;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CacheDetailDTO {

    private String cacheName;
    private Object key;
    private Object value;

    private long hitCount;
    private long missCount;

    private LocalDateTime createTime;
}