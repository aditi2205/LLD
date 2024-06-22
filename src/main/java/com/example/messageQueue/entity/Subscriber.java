package com.example.messageQueue.entity;

//this is my observable
public interface Subscriber {

    public void update(Message message);
}
