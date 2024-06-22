package com.example.messageQueue;

import com.example.messageQueue.entity.Message;
import com.example.messageQueue.entity.Subscriber;
import com.example.messageQueue.entity.Topic;
import com.example.messageQueue.service.SubscriberImpl;
import com.example.messageQueue.service.TopicFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MessageQueueApplication {

	public static void main(String[] args) {
		TopicFactory topicFactory = new TopicFactory();
		Topic uber = TopicFactory.getTopic("uber");
		Topic linkedIn = TopicFactory.getTopic("linkedIn");

		SubscriberImpl subscriber1 = new SubscriberImpl("sub-1");
		Subscriber subscriber2 = new SubscriberImpl("sub-2");

		uber.subscribe(subscriber1);
		linkedIn.subscribe(subscriber2);

		uber.publish(new Message("123", "You have been selected"));
		uber.publish(new Message("124", "Your next round is system design"));

		linkedIn.publish(new Message("124", "Your next round is system design"));
		linkedIn.publish(new Message("125", "Your next round is system design"));


		//SpringApplication.run(MessageQueueApplication.class, args);
	}

}
