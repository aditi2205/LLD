package com.example.messageQueue.service;

import com.example.messageQueue.entity.Topic;
import lombok.Synchronized;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//to support multiple topics
public class TopicFactory {

    private static final Map<String, Topic> topics = new ConcurrentHashMap<>();

//    public TopicFactory(){
//        topics = new ConcurrentHashMap<>();
//    }

    public static synchronized Topic getTopic(String topicName){
        if(!topics.containsKey(topicName)){
            Topic topic = new ConcreteTopic(topicName);
            topics.put(topicName, topic);
        }
        return topics.get(topicName);
    }

}
