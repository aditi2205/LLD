# Message Queue System

This is a simple message queue system that supports multiple topics and subscribers. The system allows for publishing messages to a specific topic and subscribing to a topic to receive messages in real-time.

## Table of Contents

- [Usage](#usage)
- [Components](#components)
- [Limitations](#limitations)
- [Contributing](#contributing)
- [License](#license)

## Usage

To use the message queue system, you first need to create a `TopicFactory` instance. The `TopicFactory` is responsible for managing multiple topics in the system.

```
TopicFactory topicFactory = new TopicFactory();
```

Once you have a TopicFactory instance, you can create new topics and subscribe to them.

```
Topic newsTopic = topicFactory.getTopic("news");
Subscriber subscriber1 = new Subscriber("Subscriber 1");
newsTopic.subscribe(subscriber1);
```
You can publish messages to a topic using the publish()method.
```
Message message = new Message("Hello, world!");
newsTopic.publish(message);
```
Subscribers can receive messages in real-time using the update() method. The system uses a background thread to continuously monitor the topic for new messages and notify subscribers when new messages are available.

```
public class Subscriber1 implements Subscriber {

  private final String name;
  private final List<IMessage> messages;

  public Subscriber(String name) {
    this.name = name;
    this.messages = new CopyOnWriteArrayList<>();
  }

  @Override
  public void update(IMessage message) {
    if (message != null) {
      messages.add(message);
    }
  }

  public void consumeMessages() {
    for (IMessage message : messages) {
      System.out.println(name + ": " + message.getBody());
    }
  }

}
```
You can also unsubscribe from a topic using the unsubscribe()method.

```
newsTopic.unsubscribe(subscriber1);
```
For more usage examples, please see the Demo class in the code.

## Components
The message queue system consists of the following components

### Topic
An interface that represents a topic in the system. A topic can have multiple subscribers and can receive messages.

### AbstractTopic
An abstract class that implements the Topic interface. This class encapsulates the common logic of managing the subscribers, offsets, and message queue.

### ConcreteTopic
A concrete implementation of AbstractTopic that specifies the name of the topic.

### ISubscriber
An interface that represents a subscriber in the system. A subscriber can receive messages from one or more topics.

### Subscriber
An implementation of ISubscriber that stores received messages in a buffer and provides a method to consume them.

### Message
An implementation of message that contains id and content.

### TopicFactory
A factory class that manages multiple topics in the system.

## Limitations
The current implementation of the message queue system has several limitations, including:

The system does not provide any message routing or filtering capabilities.

The system does not support persistent storage of messages, so all messages are lost if the system is restarted.

The system may experience performance issues if the number of subscribers or messages becomes very large.
