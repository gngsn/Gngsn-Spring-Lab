package com.gngsn.ratelimit.algorithm;

public class TokenBucket {

    private final long maxBucketSize;
    private final long refillRate;
    private double currentBucketSize;
    private long lastRefillTimestamp;

    public TokenBucket(final long maxBucketSize, final long refillRate) {
        this.maxBucketSize = maxBucketSize;
        this.refillRate = refillRate;

        this.currentBucketSize = maxBucketSize;
        this.lastRefillTimestamp = nanoTime();
    }


    public synchronized boolean allow(int tokens) {
        refillTokens();

        if (currentBucketSize > tokens) {
            currentBucketSize -= tokens;

            return true;
        }

        return false;
    }

    private void refillTokens() {
        final long now = nanoTime();

        final double tokensToAdd = (now - this.lastRefillTimestamp) * refillRate /1e9;
        this.currentBucketSize = Math.min(this.currentBucketSize + tokensToAdd, this.maxBucketSize);
        this.lastRefillTimestamp = now;

    }

    private long nanoTime() {
        return System.nanoTime();
    }
}