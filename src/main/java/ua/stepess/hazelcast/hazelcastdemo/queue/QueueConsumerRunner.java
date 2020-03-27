package ua.stepess.hazelcast.hazelcastdemo.queue;

import ua.stepess.hazelcast.hazelcastdemo.queue.service.ConsumerMember;

import static ua.stepess.hazelcast.hazelcastdemo.Constants.QUEUE_NAME;

public class QueueConsumerRunner {

    public static void main(String[] args) throws Exception {
        ConsumerMember consumerMember = new ConsumerMember();
        consumerMember.consume(QUEUE_NAME, args[0]);
    }

}
