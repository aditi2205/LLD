package com.example.rateLimiter;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public class TokenBucket {
    private int tokens;
    private int bucketSize;
    private LocalDateTime lastRefillDatetime;
    private int refillRate; //how many tokens need to be added in the bucket every second

    public TokenBucket(int bucketSize, int refillRate){
        this.tokens = refillRate;
        this.bucketSize = bucketSize;
        this.lastRefillDatetime = LocalDateTime.now();
        this.refillRate = refillRate;
    }

    private synchronized void refillTokens(){
        //refill the tokens based on how many seconds have elapsed
        //we will fill x tokens every second
        LocalDateTime now = LocalDateTime.now();
        final long timeElapsedSeconds = lastRefillDatetime.until(now, ChronoUnit.SECONDS);
        int tokensToAdd = (int) ((timeElapsedSeconds) * (refillRate));
        if(tokensToAdd > 0) {
            this.tokens = Math.min(this.tokens + tokensToAdd, bucketSize);
            lastRefillDatetime = LocalDateTime.now();
        }

    }

    public boolean consumeTokens(int tokensToConsume){
        refillTokens();
        if(tokens > tokensToConsume){
            this.tokens = Math.max(this.tokens - tokensToConsume, 0);
            return true;
        }
       return false;
    }

    public synchronized int getTokens(){
        refillTokens();
        return this.tokens;
    }
}
