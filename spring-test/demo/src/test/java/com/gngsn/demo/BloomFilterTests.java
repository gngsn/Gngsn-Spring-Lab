package com.gngsn.demo;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.stream.IntStream;

public class BloomFilterTests {

    @Test
    public void integerTest() {
        BloomFilter<Integer> filter = BloomFilter.create(
            Funnels.integerFunnel(), 500, 0.01);
        filter.put(1);
        filter.put(2);
        filter.put(3);

//        IntStream.range(0, 100_000).forEach(filter::put);

        Assertions.assertTrue(filter.mightContain(2));
        Assertions.assertTrue(filter.mightContain(1));
        Assertions.assertTrue(filter.mightContain(3));

        Assertions.assertFalse(filter.mightContain(100));
    }

    @Test
    public void stringTest() {
        BloomFilter<String> blackListedIps = BloomFilter.create(
            Funnels.stringFunnel(Charset.forName("UTF-8")), 500, 0.01);

        blackListedIps.put("192.170.0.1");
        blackListedIps.put("75.245.10.1");
        blackListedIps.put("10.125.22.20");

        Assertions.assertTrue(blackListedIps.mightContain("75.245.10.1"));
    }


}
