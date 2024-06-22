package com.example.rateLimiter;

public interface RateLimiter {
    public boolean allow (String customerId, int tokens);
}
