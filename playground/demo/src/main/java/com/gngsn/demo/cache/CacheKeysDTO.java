package com.gngsn.demo.cache;

public class CacheKeysDTO {
    private String cacheName;
    private Object keys;

    public CacheKeysDTO() {
    }

    public CacheKeysDTO(final String cacheName, final Object keys) {
        this.cacheName = cacheName;
        this.keys = keys;
    }

    public void setCacheName(final String cacheName) {
        this.cacheName = cacheName;
    }

    public void setKeys(final Object keys) {
        this.keys = keys;
    }
}
