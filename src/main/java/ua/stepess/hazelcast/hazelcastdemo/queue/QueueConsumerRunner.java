package ua.stepess.hazelcast.hazelcastdemo.queue;

import ua.stepess.hazelcast.hazelcastdemo.queue.service.ConsumerMember;

public class QueueConsumerRunner {

    public static final String QUEUE_NAME = "queue";

    public static void main(String[] args) throws Exception {
        ConsumerMember consumerMember = new ConsumerMember();
        consumerMember.consume(QUEUE_NAME, args[0]);
    }

}
