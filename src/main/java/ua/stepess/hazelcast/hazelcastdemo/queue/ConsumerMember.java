package ua.stepess.hazelcast.hazelcastdemo.queue;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;

public class ConsumerMember {

    public static final String QUEUE_NAME = "queue";

    public static void main(String[] args ) throws Exception {
        var config = new ClientConfig();

        var address = "localhost:" + args[0];
        config.getNetworkConfig()
                .addAddress(address);

        HazelcastInstance hz = HazelcastClient.newHazelcastClient(config);
        IQueue<Integer> queue = hz.getQueue(QUEUE_NAME);
        while ( true ) {
            int item = queue.take();
            System.out.println( "Consumed: " + item );
            if ( item == -1 ) {
                queue.put( -1 );
                break;
            }
            Thread.sleep( 2000 );
        }
        System.out.println( "Consumer Finished!" );
    }
}
