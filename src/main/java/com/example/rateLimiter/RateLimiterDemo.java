package com.example.rateLimiter;

import java.util.Random;

public class RateLimiterDemo {
    private static final int NUM_REQUESTS = 100;
    private static final int REQUEST_RATE = 10;
    private static final int BUCKET_SIZE = 20;
    private static final int REFILL_RATE = 5;

    public static void main(String[] args) throws InterruptedException {
        TokenBucketRateLimiterImpl rateLimiter = TokenBucketRateLimiterImpl.getInstance();
        int successfulRequests = 0;
        Random random = new Random();

        // Generate requests and try to perform them at a fixed rate
        for (int i = 0; i < NUM_REQUESTS; i++) {
            Thread.sleep(1000 / REQUEST_RATE);
            boolean allowed = rateLimiter.allow("User123", 1); // try to consume 1 token
            if (allowed) {
                successfulRequests++;
                System.out.println("Request Successful! Tokens remaining: " + rateLimiter.getTokens("User123"));
            } else {
                System.out.println("Request Rejected! Tokens remaining: " + rateLimiter.getTokens("User123"));
            }
        }

        // Print the results of the requests
        System.out.println("\nTotal Requests: " + NUM_REQUESTS);
        System.out.println("Successful Requests: " + successfulRequests);
        System.out.println("Rejected Requests: " + (NUM_REQUESTS - successfulRequests));
    }
}
