package ua.stepess.hazelcast.hazelcastdemo.queue;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;

public class ProducerMember {

    public static final String QUEUE_NAME = "queue";

    public static void main(String[] args ) throws Exception {
        var config = new ClientConfig();

        var address = "localhost:5701";
        config.getNetworkConfig()
                .addAddress(address);

        HazelcastInstance hz = HazelcastClient.newHazelcastClient(config);
        IQueue<Integer> queue = hz.getQueue(QUEUE_NAME);
        for ( int k = 1; k < 100; k++ ) {
            queue.put( k );
            System.out.println( "Producing: " + k );
            Thread.sleep(500);
        }
        queue.put( -1 );
        System.out.println( "Producer Finished!" );
    }
}
