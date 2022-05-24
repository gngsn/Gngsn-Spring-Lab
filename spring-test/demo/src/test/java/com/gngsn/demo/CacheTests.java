package com.gngsn.demo;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.gngsn.demo.common.user.UserVO;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;

import javax.annotation.PostConstruct;

@Slf4j
@Log4j2
@SpringBootTest
public class CacheTests {

  @Autowired
  CacheManager cacheManager;

  @PostConstruct
  public void init() {
    cacheManager.getCache("users").putIfAbsent("selectUserByName_test1", new UserVO("test1", "test1@email.com", "qwerty"));
    cacheManager.getCache("users").putIfAbsent("selectUserByName_test2", new UserVO("test2", "test2@email.com", "qwerty"));
    cacheManager.getCache("users").putIfAbsent("selectUserByName_test3", new UserVO("test3", "test3@email.com", "qwerty"));
    cacheManager.getCache("views").putIfAbsent("views_user1", 2);
    cacheManager.getCache("views").putIfAbsent("views_user2", 5);
    cacheManager.getCache("views").putIfAbsent("views_user4", 12);
    cacheManager.getCache("views").putIfAbsent("views_user6", 11);
    cacheManager.getCache("views").putIfAbsent("views_user4", 12);
    cacheManager.getCache("views").putIfAbsent("views_user6", 11);
    cacheManager.getCache("views").putIfAbsent("views_user6", 11);
    cacheManager.getCache("views").putIfAbsent("views_user6", 11);
  }

  @Test
  public void getAllCaches() {
    for (String cacheName : cacheManager.getCacheNames()) {
      log.info(cacheName);
    }

    /*   Functional

    cacheManager.getCacheNames().forEach(log::info);
    */
  }

  @Test
  public void getAllKeyAndValue() {
    for (String cacheName : cacheManager.getCacheNames()) {

      Cache cache = ((CaffeineCache) cacheManager.getCache(cacheName)).getNativeCache();
      log.info("cacheName: {}", cacheName);
      for (Object key: cache.asMap().keySet()) {
        Object value = cache.getIfPresent(key);

        log.info("  key: {} - value: {}", key, value.toString());
      }
    }

    /*   Functional

    cacheManager.getCacheNames()
        .stream()
          .map(cacheName -> ((CaffeineCache) cacheManager.getCache(cacheName)).getNativeCache())
          .forEach(cache -> cache.asMap().keySet().forEach(key -> {
            log.info("  key: {} - value: {}", key, cache.getIfPresent(key).toString());
          }));
     */
  }

  @Test
  public void getCachesStats() {
    for (String cacheName : cacheManager.getCacheNames()) {
      Cache cache = ((CaffeineCache) cacheManager.getCache(cacheName)).getNativeCache();
      CacheStats stats = cache.stats();
      log.info("cache '{}' - stats : {}", cacheName, stats.toString());
    }
  }

  public void beforeCache() {
    log.info("-- before -- ");
    getAllKeyAndValue();
  }

  @Test
  public void removeAllCaches() {
    beforeCache();
    for (String cacheName : cacheManager.getCacheNames()) {
      cacheManager.getCache(cacheName).clear();
    }
    afterCache();
  }

  public void afterCache() {
    log.info("-- after -- ");
    getAllKeyAndValue();
  }


  @Test
  public void removeTargetCache() {
    String targetCacheName = "views";
    beforeCache();

    ((CaffeineCache)cacheManager.getCache(targetCacheName)).clear();

    afterCache();
  }

  @Test
  public void removeTargetKey() {
    String targetCacheName = "views";
    String targetCacheKey = "views_user3";
    log.info("-- before -- ");
    getAllKeyAndValue();
    ((CaffeineCache)cacheManager.getCache(targetCacheName)).evict(targetCacheKey);
    log.info("-- after -- ");
    getAllKeyAndValue();
  }
}
