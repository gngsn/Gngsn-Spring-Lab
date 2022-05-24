package com.gngsn.demo.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum CacheType {
    USERS(ConstName.USERS),
    VIEWS(ConstName.VIEWS, 2000, 1);

    private String name;
    private int expireAfterWrite;
    private int maximumSize;

    CacheType(String name) {
        this.name = name;
        this.expireAfterWrite = ConstantConfig.DEFAULT_TTL;
        this.maximumSize = ConstantConfig.DEFAULT_MAX_SIZE;
    }

    static class ConstantConfig {
        static final int DEFAULT_TTL = 3000;
        static final int DEFAULT_MAX_SIZE = 10000;
    }

    public static class ConstName {
        static final String USERS = "users";
        static final String VIEWS = "views";
    }
}

