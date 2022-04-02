package com.gngsn.demo.cache;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.gngsn.demo.common.ResJson;
import lombok.RequiredArgsConstructor;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("monitor/caches")
@RequiredArgsConstructor
public class CacheController {

    private final CacheManager cacheManager;

    /**
     * 등록된 모든 캐시된 키 정보들을 조회
     */
    @GetMapping("/keys")
    public ResJson cacheKeys() {

        List<CacheKeysDTO> result = new ArrayList<>();
        for (String cacheName : cacheManager.getCacheNames()) {
            CacheKeysDTO cacheKeysDTO = new CacheKeysDTO();

            cacheKeysDTO.setCacheName(cacheName);

            CaffeineCache caffeineCache = (CaffeineCache) cacheManager.getCache(cacheName);
            Cache cache = caffeineCache.getNativeCache();

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

            CaffeineCache caffeineCache = (CaffeineCache) cacheManager.getCache(cacheName);
            Cache cache = caffeineCache.getNativeCache();
            CacheStats stats = cache.stats();

            for (Object key: cache.asMap().keySet()) {
                CacheDetailDTO detailDTO = new CacheDetailDTO();
                Object value = cache.getIfPresent(key);

                detailDTO.setCacheName(cacheName);
                detailDTO.setKey(key);
                detailDTO.setValue(value);
                detailDTO.setHitCount(stats.hitCount());
                detailDTO.setMissCount(stats.missCount());
                detailDTO.setTotalLoadTime(stats.totalLoadTime());
                detailDTO.setEvictionCount(stats.evictionCount());
                detailDTO.setEvictionWeight(stats.evictionWeight());

                detailList.add(detailDTO);
            }
        }

        HashMap<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("totalCnt", detailList.size());
        rtnMap.put("detailList", detailList);

        return new ResponseEntity<>(rtnMap, HttpStatus.OK);
    }

//    /**
//     * Ehcache의 모든 캐시를 제거
//     *
//     * @param authKey
//     * @return
//     */
//    @PostMapping("/clear/all")
//    public ResponseEntity<HashMap> clearAllCache(@AuthKeyValid String authKey) {
//
//        List<String> cacheNameList = new LinkedList<>();
//        for (String cacheName : cacheManager.getCacheNames()) {
//            cacheNameList.add(cacheName);
//            Objects.requireNonNull(cacheManager.getCache(cacheName)).clear();
//        }
//
//        HashMap<String, Object> rtnMap = new HashMap<>();
//        rtnMap.put("msg", "success remove all cache");
//        rtnMap.put("removedCacheNameList", cacheNameList);
//
//        return new ResponseEntity<>(rtnMap, HttpStatus.OK);
//
//    }
//
//    /**
//     * 특정 캐시 1건을 clear
//     *
//     * @param authKey
//     * @param cacheName
//     * @return
//     */
//    @PostMapping("/clear/target")
//    public ResponseEntity<String> clearTargetCache(@AuthKeyValid String authKey, @RequestParam String cacheName) {
//
//        try {
//
//            if (StringUtils.isBlank(cacheName)) {
//                return new ResponseEntity<>(String.format("cacheName '%s' is blank.", cacheName), HttpStatus.BAD_REQUEST);
//            }
//
//            Cache targetCache = cacheManager.getCache(cacheName);
//            if (targetCache == null) {
//                return new ResponseEntity<>(String.format("'%s' cache not exist. Check cacheName.", cacheName), HttpStatus.BAD_REQUEST);
//            }
//
//            targetCache.clear(); //해당 캐시 clear
//
//            return new ResponseEntity<>(String.format("'%s' clear succes.", cacheName), HttpStatus.OK);
//
//        } catch (Exception e) {
//            log.error("Exception msg:{} | stack trace:", e.getMessage(), e);
//            return new ResponseEntity<>(String.format("'%s' cache clear FAIL", cacheName), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

}