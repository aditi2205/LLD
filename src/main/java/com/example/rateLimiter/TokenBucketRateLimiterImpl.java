package com.example.rateLimiter;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//we will make it singleton so that there is only one rateLimiter per customer
public class TokenBucketRateLimiterImpl implements RateLimiter{

    //Map of customer and their buckets
    Map<String, TokenBucket> customerBucketMap;
    //bucketsize and refill rate limit
    public final int bucketSize;
    public final int refillRate; // how many tokens need to be refilled in the bucket every second

    public static final int DEFAULT_BUCKET_SIZE = 20;
    public static final int DEFAULT_RATE_LIMIT = 5;
    public static volatile TokenBucketRateLimiterImpl rateLimiter;

    private TokenBucketRateLimiterImpl(int bucketSize, int refillRate) {
        customerBucketMap = new ConcurrentHashMap<>();
        this.bucketSize = bucketSize;
        this.refillRate = refillRate;
    }

    public static TokenBucketRateLimiterImpl getInstance(){
        if (rateLimiter == null){
            synchronized (TokenBucketRateLimiterImpl.class){
                if(rateLimiter == null){
                    rateLimiter = new TokenBucketRateLimiterImpl(DEFAULT_BUCKET_SIZE, DEFAULT_RATE_LIMIT);
                }
            }
        }
        return rateLimiter;
    }

    @Override
    public boolean allow(String customerId, int tokens) {
        //when are we going to allow: if we have enough tokens in our bucket
        synchronized (customerBucketMap){
            if(!customerBucketMap.containsKey(customerId)) {
                customerBucketMap.put(customerId, new TokenBucket(bucketSize, refillRate));
            }
        }
        TokenBucket bucket = customerBucketMap.get(customerId);
        boolean allowed = bucket.consumeTokens(tokens);
        return allowed;
    }

    public int getTokens(String customerId){
        synchronized (customerBucketMap){
            if(!customerBucketMap.containsKey(customerId)) {
                customerBucketMap.put(customerId, new TokenBucket(bucketSize, refillRate));
            }
        }
        return customerBucketMap.get(customerId).getTokens();
    }
}
