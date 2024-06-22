package com.example.circuitbreaker;

import com.example.circuitbreaker.impl.CircuitBreaker;
import com.example.circuitbreaker.impl.LoggingCircuitBreakerListener;

import java.util.Random;

public class CircuitBreakerApplication {
    public static class MyService {
        private CircuitBreaker circuitBreaker;

        public MyService(CircuitBreaker circuitBreaker) {
            this.circuitBreaker = circuitBreaker;
        }

        public void doSomething(String parameter) {
            try {
                if (circuitBreaker.allowRequest()) {
                    // perform your service logic here
                    // ...
                    System.out.println("in mock service call");
                    mockServiceCall();

                    circuitBreaker.recordSuccess();

                } else {
                    // the circuit breaker is open
                    // handle the error case
                    System.out.println("Circuit open: Simply return");
                    return ;
                }
            } catch (Exception e) {
                // handle the exception
                System.out.println("Record failure");
                circuitBreaker.recordFailure(e);
            }
        }

        public boolean mockServiceCall() throws Exception {
            throw new Exception("Dummy exception for failure");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CircuitBreaker circuitBreaker = new CircuitBreaker(3, 3000, new LoggingCircuitBreakerListener());

        MyService myService = new MyService(circuitBreaker);

        for(int i=0; i<10; i++) {
            myService.doSomething("parameters");
            Thread.sleep(2000);
        }
    }

}
