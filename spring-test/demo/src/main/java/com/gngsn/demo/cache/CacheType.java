package com.gngsn.demo.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum CacheType {
    USERS("users", 10, 10000),
    BOOKS("views", 10, 1000);

    private String name;
    private int expireAfterWrite = ConstantConfig.DEFAULT_TTL;
    private int maximumSize = ConstantConfig.DEFAULT_MAX_SIZE;
}


class ConstantConfig {
    static final int DEFAULT_TTL = 3000;
    static final int DEFAULT_MAX_SIZE = 10000;
}