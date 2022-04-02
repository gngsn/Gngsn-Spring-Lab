package com.gngsn.demo.cache;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CacheKeysDTO {
    private String cacheName;
    private Object keys;
//    private List<Object> keys;
}
