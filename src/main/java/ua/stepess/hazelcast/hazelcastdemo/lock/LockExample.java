package ua.stepess.hazelcast.hazelcastdemo.lock;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.cp.lock.FencedLock;

import static java.util.concurrent.CompletableFuture.runAsync;
import static ua.stepess.hazelcast.hazelcastdemo.Constants.LOCK_NAME;

public class LockExample {

    public static void main(String[] args) throws InterruptedException {
        var config = new ClientConfig();

        var address = "localhost:5701";
        config.getNetworkConfig()
                .addAddress(address);

        var hazelcastInstance = HazelcastClient.newHazelcastClient(config);

        var lock = hazelcastInstance.getCPSubsystem().getLock(LOCK_NAME);
        runAsync(() -> {
            getLockAndSleep(lock);
        });

        System.out.println("Getting lock for main job");
        lock.lock();
        System.out.println("Got lock for main job");
        try {
            System.out.println("Do main job");
            Thread.sleep(1000);
        } finally {
            System.out.println("Finished main job");
            lock.unlock();
        }
    }

    private static void getLockAndSleep(FencedLock lock){
        System.out.println("Getting lock for async job");
        lock.lock();
        System.out.println("Got lock for async job");
        try {
            System.out.println("In lock. Do some job...");
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            System.out.println("Finished async job");
            lock.unlock();
        }
    }
}
