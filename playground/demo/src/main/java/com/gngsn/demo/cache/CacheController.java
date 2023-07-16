package com.gngsn.demo.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Policy;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.gngsn.demo.common.ResJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("monitor/caches")
public class CacheController {

    public static final Logger log = LoggerFactory.getLogger(CacheConfig.class);
    private final CacheManager cacheManager;

    public CacheController(final CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * 등록된 모든 캐시된 키 정보들을 조회
     */
    @GetMapping("/keys")
    public ResJson cacheKeys() {

        List<CacheKeysDTO> result = new ArrayList<>();
        for (String cacheName : cacheManager.getCacheNames()) {
            CacheKeysDTO cacheKeysDTO = new CacheKeysDTO();

            cacheKeysDTO.setCacheName(cacheName);
            Cache cache = ((CaffeineCache) cacheManager.getCache(cacheName)).getNativeCache();

            log.info("keySet : {}", cache.asMap().keySet().toString());
            cacheKeysDTO.setKeys(cache.asMap().keySet());
            result.add(cacheKeysDTO);
        }

        return new ResJson(200, "Success get cache keys", result);
    }


    /**
     * 현재 캐시된 상세 정보들을 조회
     */
    @GetMapping("/details")
    public ResponseEntity<HashMap> cachedDetail() {

        List<CacheDetailDTO> detailList = new LinkedList<>();
        for (String cacheName : cacheManager.getCacheNames()) {
            CacheDetailDTO detailDTO = new CacheDetailDTO();

            Cache cache = ((CaffeineCache) cacheManager.getCache(cacheName)).getNativeCache();
            CacheStats stats = cache.stats();

            detailDTO.setCacheName(cacheName);
            detailDTO.setHitCount(stats.hitCount());
            detailDTO.setMissCount(stats.missCount());

            for (Object key : cache.asMap().keySet()) {
                Object value = cache.getIfPresent(key);

                detailDTO.setKey(key);
                detailDTO.setValue(value);
                detailDTO.setCreateTime(ageOfEntryUsingAfterWritePolicy(cache, key));
                log.info("key '{}' - value : {}", key, value.toString());
            }
            detailList.add(detailDTO);
        }

        HashMap<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("totalCnt", detailList.size());
        rtnMap.put("detailList", detailList);

        return new ResponseEntity<>(rtnMap, HttpStatus.OK);
    }

    @GetMapping("/stats")
    public Map stats() {
        Map<String, CacheStatsDTO> stat = new HashMap<>();

        for (String cacheName : cacheManager.getCacheNames()) {
            CacheStatsDTO cacheStatsDTO = new CacheStatsDTO();

            Cache cache = ((CaffeineCache) cacheManager.getCache(cacheName)).getNativeCache();
            CacheStats stats = cache.stats();

            log.info("cache '{}' - stats : {}", cacheName, stats.toString());
            BeanUtils.copyProperties(stats, cacheStatsDTO);
            stat.put(cacheName, cacheStatsDTO);
        }

        return stat;
    }

    private static LocalDateTime ageOfEntryUsingAfterWritePolicy(Cache cache, Object key) {
        Optional<Policy.FixedExpiration> expirationPolicy = cache.policy().expireAfterWrite();

        if (!expirationPolicy.isPresent()) {
            return null;
        }

        Optional<Duration> ageOfEntry = expirationPolicy.get().ageOf(key);
        if (!ageOfEntry.isPresent()) {
            return null;
        }

        Duration duration = ageOfEntry.get();
        log.info("Caffeine Cache: [{}] key is in the cache from {} s", key, duration.getSeconds());
        return LocalDateTime.now().minus(duration);
//        return LocalDateTime.now().minus(duration).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Ehcache의 모든 캐시를 제거
     */
    @PostMapping("/clear/all")
    public ResponseEntity<HashMap> clearAllCache() {

        List<String> cacheNameList = new LinkedList<>();

        for (String cacheName : cacheManager.getCacheNames()) {
            cacheNameList.add(cacheName);
            Objects.requireNonNull(cacheManager.getCache(cacheName)).clear();
        }

        HashMap<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("msg", "success remove all cache");
        rtnMap.put("removedCacheNameList", cacheNameList);

        return new ResponseEntity<>(rtnMap, HttpStatus.OK);

    }

    /**
     * 특정 캐시 1건을 clear
     */
    @PostMapping("/clear/target")
    public ResponseEntity<String> clearTargetCache(@RequestParam String cacheName) {

        try {
            ((CaffeineCache) cacheManager.getCache(cacheName)).clear();

            return new ResponseEntity<>(String.format("'%s' clear succes.", cacheName), HttpStatus.OK);

        } catch (Exception e) {
            log.error("Exception msg:{} | stack trace:", e.getMessage(), e);
            return new ResponseEntity<>(String.format("'%s' cache clear FAIL", cacheName), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}