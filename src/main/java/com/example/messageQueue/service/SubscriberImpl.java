package com.example.messageQueue.service;

import com.example.messageQueue.entity.Message;
import com.example.messageQueue.entity.Subscriber;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class SubscriberImpl implements Subscriber {
    private final String name;
    private static final BlockingQueue<Message> messages = new LinkedBlockingQueue<>();

    public SubscriberImpl(String name) {
        this.name = name;
    }

    @Override
    public void update(Message message) {
        messages.add(message);
        consumeMessage();
    }

    public static void consumeMessage(){
        while(true){
            try{
                Message message = messages.poll(1, TimeUnit.SECONDS);
                if(message != null){
                    System.out.println("Received message "+ message.getContent());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
