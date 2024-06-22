package com.example.circuitbreaker.impl;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CircuitBreaker {
    private final int failureThreshold;
    private final int timeout;
    private final CircuitBreakerListener listener;
    private final AtomicReference<State> state = new AtomicReference<>(new ClosedState());

    private final Lock lock = new ReentrantLock();

    public CircuitBreaker(int failureThreshold, int timeout, CircuitBreakerListener listener) {
        this.failureThreshold = failureThreshold;
        this.timeout = timeout;
        this.listener = listener;
    }

    public boolean allowRequest() {
        System.out.println(state.get().allowRequest());
        return state.get().allowRequest();
    }

    public void recordSuccess() {
        try {
            lock.lock();
            state.get().recordSuccess();
        }finally{
            lock.unlock();
        }
    }

    public void recordFailure(Exception e) {
        try {
            lock.lock();
            state.get().recordFailure(e);
        }finally {
            lock.unlock();
        }
    }

    public void reset() {
        state.set(new ClosedState());
    }



    //using the state pattern

    private interface State {
        //if this returns true only then actual service call will be made else it will directly return error
        boolean allowRequest();

        //will contain the state update logic
        void recordSuccess();
        //will contain the state update logic
        void recordFailure(Exception e);

        //start with closed state, if failures exceed threshold then go to open state
        // in open state wait for the timeout time, then switch to half open
        // in half open, if request is success then move to closed else move to open
    }

    private class ClosedState implements State {
        private int failures = 0;

        @Override
        public boolean allowRequest() {
            return true;
        }

        //happen inside a lock
        @Override
        public void recordSuccess() {
            //resetting the failure count
            failures = 0;
        }

        //happen inside a lock
        @Override
        public void recordFailure(Exception e) {
            System.out.println("recording failure in closed state");
            failures++;
            if (failures >= failureThreshold) {
                state.set(new OpenState());  //updating the state to open
                listener.onStateChange(StateType.OPEN);
            }
        }
    }

    private class OpenState implements State {
        private long expiryTime; //after how long we will try the service again

        public OpenState() {
            this.expiryTime = System.currentTimeMillis() + timeout;
        }

        @Override
        public boolean allowRequest() {
            if (System.currentTimeMillis() >= expiryTime) {
                state.set(new HalfOpenState());   //first change to half open to see if service comes up
                listener.onStateChange(StateType.HALF_OPEN);
                return true;
            }
            return false;
        }

        @Override
        public void recordSuccess() {}

        @Override
        public void recordFailure(Exception e) {}
    }

    private class HalfOpenState implements State {
        @Override
        public boolean allowRequest() {
            return true;
        }

        @Override
        public void recordSuccess() {
            //if successful in half open, the close the circuit again
            state.set(new ClosedState());
            listener.onStateChange(StateType.CLOSED);
        }

        @Override
        public void recordFailure(Exception e) {
            // if unsuccessful, bounce back to open state and again wait for timeout
            state.set(new OpenState());
            listener.onStateChange(StateType.OPEN);
        }
    }
}
