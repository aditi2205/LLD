package com.example.circuitbreaker.impl;

public class LoggingCircuitBreakerListener implements CircuitBreakerListener{
    @Override
    public void onStateChange(StateType stateType) {
        System.out.println("Circuit breaker State changed to "+ stateType);
    }
}
