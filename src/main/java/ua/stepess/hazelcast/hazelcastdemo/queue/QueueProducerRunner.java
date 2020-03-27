package ua.stepess.hazelcast.hazelcastdemo.queue;

import ua.stepess.hazelcast.hazelcastdemo.queue.service.ProducerMember;

import static ua.stepess.hazelcast.hazelcastdemo.Constants.QUEUE_NAME;

public class QueueProducerRunner {

    public static void main(String[] args) throws Exception {
        ProducerMember producerMember = new ProducerMember();
        producerMember.produce(QUEUE_NAME);
    }
}
