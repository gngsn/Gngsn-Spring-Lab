package com.gngsn.ratelimit.algorithm;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FixedWindowTest {
    private final int NANO_TO_ONE_SEC = 1000 * 1000 * 1000;
    private final int NANO_TO_ONE_MILLI_SEC = 1000 * 1000;

    @Test
    public void test() throws InterruptedException {
        FixedWindow fixedWindow = new FixedWindow(NANO_TO_ONE_SEC, 10); // 2sec 10

        Assertions.assertTrue(fixedWindow.allow());
        long startMilliSec = System.currentTimeMillis();

        Assertions.assertTrue(fixedWindow.allow());
        Assertions.assertTrue(fixedWindow.allow());
        Assertions.assertTrue(fixedWindow.allow());
        Assertions.assertTrue(fixedWindow.allow());
        Assertions.assertTrue(fixedWindow.allow());
        Assertions.assertTrue(fixedWindow.allow());
        Assertions.assertTrue(fixedWindow.allow());
        Assertions.assertTrue(fixedWindow.allow());
        Assertions.assertTrue(fixedWindow.allow());
        Assertions.assertFalse(fixedWindow.allow());

        long oneSecSinceStart = (System.currentTimeMillis() - startMilliSec) * 1_000;
        Thread.sleep(oneSecSinceStart);
        Assertions.assertTrue(fixedWindow.allow());

    }
}