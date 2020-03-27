package ua.stepess.hazelcast.hazelcastdemo.topic;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.ITopic;

import static ua.stepess.hazelcast.hazelcastdemo.Constants.TOPIC_NAME;

public class TopicPublisher {

    public static void main(String[] args) throws InterruptedException {
        var config = new ClientConfig();

        var address = "localhost:5701";
        config.getNetworkConfig()
                .addAddress(address);

        var hz = HazelcastClient.newHazelcastClient(config);
        ITopic<Integer> topic = hz.getTopic(TOPIC_NAME);

        System.out.println("Started publishing");

        for (int i = 0; i < 20; i++) {
            topic.publish(i);
            System.out.println("Published: " + i);
            Thread.sleep(1000);
        }

        System.out.println("Finished publishing");
    }
}
