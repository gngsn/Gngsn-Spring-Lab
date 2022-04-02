package com.gngsn.demo.cache;

import lombok.Getter;

@Getter
public enum CacheType {
    USERS( "users", // 캐시 이름: users
            5 * 60, // 만료 시간: 5 분
            10000 // 최대 갯수: 10000
    );

    private final String name;
    private final int expire;
    private final int maxSize;

    CacheType(String name , int expire, int maxSize) {
        this.name = name;
        this.expire = expire;
        this.maxSize = maxSize;
    }
}

