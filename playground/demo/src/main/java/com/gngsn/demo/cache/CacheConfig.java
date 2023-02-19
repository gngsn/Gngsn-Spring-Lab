package com.gngsn.demo.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        List<CaffeineCache> caches = Arrays.stream(CacheType.values())
            .map(cache -> new CaffeineCache(
                    cache.getName(),
                    Caffeine.newBuilder()
                        .expireAfterWrite(cache.getExpireAfterWrite(), TimeUnit.SECONDS)
                        .maximumSize(cache.getMaximumSize())
                        .evictionListener((Object key, Object value, RemovalCause cause) -> {
                                log.info("Key {} was evicted ({}): {}", key, cause, value);
                            }
                        )
                        .removalListener((Object key, Object value, RemovalCause cause) ->
                            log.info("Key {} was removed ({}}): {}", key, cause, value))
                        .recordStats()
                        .build()
                )
            )
            .collect(Collectors.toList());

        cacheManager.setCaches(caches);
        return cacheManager;
    }
}

/**
 * Key views_user2 was evicted (SIZE): 5
 * Key views_user1 was evicted (SIZE): 2
 * Key views_user4 was evicted (SIZE): 12
 */