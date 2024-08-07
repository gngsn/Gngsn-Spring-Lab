package com.gngsn.demo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

//@SpringBootTest
public class ConcurrentHashMapTests {
    @BeforeEach
    public void setUp() {
    }

    @Test
    public void hashmap() throws InterruptedException {
        Map<Integer, String> map = new HashMap<>();

        // Thread 1: Puts entries into the map
        Thread writerThread = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                map.put(i, "Value" + i);
            }
        });

        // Thread 2: Iterates over the map
        Thread readerThread = new Thread(() -> {
            while (true) {
                for (Integer key : map.keySet()) {
                    String value = map.get(key);
                    System.out.println("key: " + key + ", value: " + value);
                }
                if (map.size() == 10000) break;
            }
        });

        // Start both threads
        writerThread.start();
        readerThread.start();

        // Wait for both threads to finish
        writerThread.join();
        readerThread.join();
    }

    @Test
    public void concurrentHashMap() throws InterruptedException {
//        System.out.println("map: ");
//        map.forEach((k, v) -> {
//            System.out.println("k: " + k + " v: " + v);
//        });
        Map<Integer, String> map = new ConcurrentHashMap<>();

        // Thread 1: Puts entries into the map
        Thread writerThread = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                map.put(i, "Value" + i);
            }
        });

        // Thread 2: Iterates over the map
        Thread readerThread = new Thread(() -> {
            while (true) {
                for (Integer key : map.keySet()) {
                    // Do something with the key
                    String value = map.get(key);
                    System.out.println("key: " + key + ", value: " + value);
                }

                if (map.size() == 10000) break;
            }
        });

        // Start both threads
        writerThread.start();
        readerThread.start();

        // Wait for both threads to finish
        writerThread.join();
        readerThread.join();
    }

    @AfterEach
    public void close() {
    }
}