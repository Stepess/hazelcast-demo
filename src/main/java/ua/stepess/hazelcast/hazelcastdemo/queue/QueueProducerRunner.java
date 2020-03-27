package ua.stepess.hazelcast.hazelcastdemo.queue;

import ua.stepess.hazelcast.hazelcastdemo.queue.service.ProducerMember;

public class QueueProducerRunner {

    public static final String QUEUE_NAME = "queue";

    public static void main(String[] args) throws Exception {
        ProducerMember producerMember = new ProducerMember();
        producerMember.produce(QUEUE_NAME);
    }
}
