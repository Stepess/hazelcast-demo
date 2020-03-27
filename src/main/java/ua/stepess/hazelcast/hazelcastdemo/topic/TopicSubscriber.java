package ua.stepess.hazelcast.hazelcastdemo.topic;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;

import static ua.stepess.hazelcast.hazelcastdemo.Constants.TOPIC_NAME;

public class TopicSubscriber {

    public static void main(String[] args) {
        var config = new ClientConfig();

        var address = "localhost:" + args[0];
        config.getNetworkConfig()
                .addAddress(address);

        var hz = HazelcastClient.newHazelcastClient();
        ITopic<Integer> topic = hz.getTopic(TOPIC_NAME);
        topic.addMessageListener(new MessageListenerImpl());
        System.out.println("Subscribed");
    }

    private static class MessageListenerImpl implements MessageListener<Integer> {
        public void onMessage(Message<Integer> m) {
            System.out.println("Received: " + m.getMessageObject());
        }
    }
}
