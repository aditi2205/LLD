## TokenBucket Class

The `TokenBucket` class represents a token bucket whose capacity is defined by the `bucketSize`. Tokens are added to the bucket at a rate specified by the `refillRate`. The class exposes two methods:

#### `consumeTokens(int tokensToConsume)`

This method tries to consume `tokensToConsume` tokens from the bucket. If the requested number of tokens are available, it will subtract them from the bucket and return `true`. Otherwise, it will do nothing and return `false`.

#### `getTokens()`

This method returns the current number of tokens available in the bucket. If any time has elapsed since the last call to this method, it refills the bucket with tokens at the specified `refillRate`.

## RateLimiter Class

The `RateLimiter` abstract class defines a simple interface for implementing rate limiters. It has two abstract methods:

#### `boolean allow()`

This method tries to allow a request through the rate limiter. If the request is allowed, it returns `true`. Otherwise, it returns `false`.

#### `int getTokens()`

This method returns the current number of tokens available in the rate limiter.

## TokenBucketRateLimiter Class

The `TokenBucketRateLimiter` class is an implementation of the `RateLimiter` interface that uses `TokenBucket` objects to enforce rate limiting for each user.

#### `boolean allow(String userId, int tokensToConsume)`

This method tries to consume `tokensToConsume` tokens from the `TokenBucket` object for the given `userId`. If the requested number of tokens are available, it will subtract them from the `TokenBucket` and return `true`. Otherwise, it will do nothing and return `false`.

#### `int getTokens(String userId)`

This method returns the current number of tokens available in the `TokenBucket` object for the given `userId`. If any time has elapsed since the last call to this method, it refills the `TokenBucket` with tokens at the specified `refillRate`.

#### `static TokenBucketRateLimiter getInstance()`

This method returns a singleton instance of the `TokenBucketRateLimiter` class.

## MultiThreadRateLimiterDemo Class

The `MultiThreadRateLimiterDemo` class is an example of how to use the `TokenBucketRateLimiter` class to rate limit a multi-threaded process that generates requests for multiple customers.

It has a `main()` method that creates a thread pool with `NUM_THREADS` threads, generates requests for `NUM_REQUESTS_PER_THREAD` requests * `NUM_THREADS` threads, and prints the final number of tokens remaining for each user.

## Conclusion

These classes can be used to implement a simple rate limiter for your application. It's important to tailor the `bucketSize` and `refillRate` parameters to the specific needs of your application to ensure that the rate limiter functions as expected. Additionally, you may need to implement protection against race conditions, depending on the threading model used in your application.