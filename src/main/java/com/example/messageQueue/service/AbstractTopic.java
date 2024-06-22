package com.example.messageQueue.service;

import com.example.messageQueue.entity.Message;
import com.example.messageQueue.entity.Subscriber;
import com.example.messageQueue.entity.Topic;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class AbstractTopic implements Topic {

    private final String name;
    //list of observables
    private final List<Subscriber> subscribers;
    private final Map<Subscriber, Integer> subscriberOffsets;
    private final BlockingQueue<Message> messageQueue;

    public AbstractTopic(String name) {
        this.name = name;
        this.subscribers = new CopyOnWriteArrayList<>();
        this.subscriberOffsets = new ConcurrentHashMap<>();
        this.messageQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
        subscriberOffsets.put(subscriber, messageQueue.size()-1);
    }

    private void processSubscriber(Subscriber subscriber){
        int offset = subscriberOffsets.get(subscriber);
        while(true){
            try{
                Message message = messageQueue.poll(1, TimeUnit.SECONDS);
                if(message != null){
                    offset++;
                    subscriberOffsets.put(subscriber, offset);
                    subscriber.update(message);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void unSubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
        subscriberOffsets.remove(subscriber);
    }

    @Override
    public void publish(Message message) {
        messageQueue.add(message);
        //for all subscribers, notify them (Observable design pattern)
        for(Subscriber subscriber : subscribers){
            new Thread(() -> processSubscriber(subscriber)).start();
        }
    }

    @Override
    public void resetOffset(Subscriber subscriber, int offset) {
        int currOffset = subscriberOffsets.get(subscriber);
        if (offset > 0 && offset < messageQueue.size()){
            subscriberOffsets.put(subscriber, offset);
            for (int i=offset; i<=currOffset; i++){
                //convert message queue to array
                Message[] messages = messageQueue.toArray(new Message[0]);
                subscriber.update(messages[i]);
            }
        }
    }
}
