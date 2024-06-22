package com.example.circuitbreaker.impl;

public interface CircuitBreakerListener {
    void onStateChange(StateType stateType);
}
