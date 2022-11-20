package com.gngsn.ratelimit;

public class TokenBucket implements RateLimiter {

    private final long maxBucketSize;
    private final long refillRate;
    private double currentBucketSize;
    private long lastRefillTimestamp;

    public TokenBucket(final long maxBucketSize, final long refillRate) {
        this.maxBucketSize = maxBucketSize;
        this.refillRate = refillRate;

        this.currentBucketSize = maxBucketSize;
        this.lastRefillTimestamp = getNowInSec();
    }

    @Override
    public boolean allow() {
        refillTokens();

        if (currentBucketSize > 0) {
            currentBucketSize --;

            return true;
        }

        return false;
    }

    private void refillTokens() {
        final long now = getNowInSec();

        final double tokensToAdd = (now - this.lastRefillTimestamp) * refillRate;
        this.currentBucketSize = Math.min(this.currentBucketSize + tokensToAdd, this.maxBucketSize);
        this.lastRefillTimestamp = now;

    }

    private long getNowInSec() {
        return System.currentTimeMillis() / 1000;
    }
}