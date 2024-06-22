package com.example.messageQueue.entity;

public interface Publisher {
    void publish(String topic, Message message);
}
