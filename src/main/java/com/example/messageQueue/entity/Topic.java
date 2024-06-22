package com.example.messageQueue.entity;

//This is my subject
public interface Topic {

    public void subscribe(Subscriber subscriber);

    public void unSubscribe (Subscriber subscriber);

    public void publish(Message message);

    public void resetOffset( Subscriber subscriber, int offset);


}
