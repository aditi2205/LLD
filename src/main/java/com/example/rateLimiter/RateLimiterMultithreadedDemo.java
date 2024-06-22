package com.example.rateLimiter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RateLimiterMultithreadedDemo {

    private static final int NUM_THREADS = 3;
    private static final int NUM_REQUESTS_PER_THREAD = 10;
    private static final int REQUEST_RATE = 2;
    private static final int BUCKET_SIZE = 5;
    private static final int REFILL_RATE = 1;

    public static void main(String[] args) throws InterruptedException {
        TokenBucketRateLimiterImpl rateLimiter = TokenBucketRateLimiterImpl.getInstance();

        // Print the final number of tokens remaining for each user
        for (int i = 0; i < NUM_THREADS; i++) {
            final String userId = "User" + i;
            System.out.println("User: " + userId + ", Tokens remaining: " + rateLimiter.getTokens(userId));
        }

        final ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

        // Generate requests for NUM_REQUESTS_PER_THREAD requests * NUM_THREADS threads
        for (int i = 0; i < NUM_THREADS; i++) {
            final String userId = "User" + i;
            executorService.submit(() -> {
                for (int j = 0; j < NUM_REQUESTS_PER_THREAD; j++) {
                    try {
                        Thread.sleep(1000 / REQUEST_RATE);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    boolean allowed = rateLimiter.allow(userId, 1); // try to consume 1 token
                    if (allowed) {
                        System.out.println("Thread: " + Thread.currentThread().getName() +
                                ", UserId: " + userId + ", Request Successful! Tokens remaining: " +
                                rateLimiter.getTokens(userId));
                    } else {
                        System.out.println("Thread: " + Thread.currentThread().getName() +
                                ", UserId: " + userId + ", Request Rejected! Tokens remaining: " +
                                rateLimiter.getTokens(userId));
                    }
                }
            });
        }

        // Shutdown the thread pool once all tasks are completed
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            Thread.sleep(500);
        }

        // Print the final number of tokens remaining for each user
        for (int i = 0; i < NUM_THREADS; i++) {
            final String userId = "User" + i;
            System.out.println("User: " + userId + ", Tokens remaining: " + rateLimiter.getTokens(userId));
        }
    }
}
