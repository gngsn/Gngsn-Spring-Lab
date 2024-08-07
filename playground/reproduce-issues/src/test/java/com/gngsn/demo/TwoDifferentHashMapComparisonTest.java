package com.gngsn.demo;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class TwoDifferentHashMapComparisonTest {
    private static final int NUM_THREADS = 100;
    private static final int NUM_ITERATIONS = 10000;

    @Test
    void test() throws InterruptedException {

        // Create instances of HashMap and ConcurrentHashMap
        Map<Integer, Integer> hashMap = new HashMap<>();
        Map<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();

        // Perform test with HashMap
        System.out.println(">>> Testing HashMap:");
        testMap(hashMap);

        // Perform test with ConcurrentHashMap
        System.out.println(">>> Testing ConcurrentHashMap:");
        testMap(concurrentHashMap);
    }

    private void testMap(Map<Integer, Integer> map) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        for (int i = 0; i < NUM_THREADS; i++) {
            executor.execute(() -> {
                for (int j = 0; j < NUM_ITERATIONS; j++) {
                    map.put(j, j);
                    Integer value = map.get(j);
                    if (value == null || value != j) {
                        System.out.println("Data inconsistency detected - Key: " + j + ", Value: " + value);
                    }
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("Test completed.");
    }

    @Test
    public void test22() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        // Key 해싱 및 데이터 삽입
        String key = "example";
        int value = 42;

        int hash = spread(key.hashCode());
        int index = (map.size() - 1) & hash;

        System.out.println("Key: " + key);
        System.out.println("Hash: " + hash);
        System.out.println("Index: " + index);

        map.put(key, value);
        System.out.println("Value for key '" + key + "': " + map.get(key));
    }

    final int HASH_BITS = 0x7fffffff; // 32비트 마스크

    final int spread(int h) {
        return (h ^ (h >>> 16)) & HASH_BITS;
    }
}
