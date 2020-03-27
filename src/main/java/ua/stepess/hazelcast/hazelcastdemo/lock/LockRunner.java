package ua.stepess.hazelcast.hazelcastdemo.lock;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.stream.Collectors.toList;
import static ua.stepess.hazelcast.hazelcastdemo.Constants.LOCK_NAME;

public class LockRunner {

    public static void main(String[] args) {
        var ports = List.of("5701", "5702", "5703");

        var clients = ports.stream()
                .map(LockRunner::getClientConfig)
                .map(HazelcastClient::newHazelcastClient)
                .collect(toList());

        var futures = IntStream.range(0, clients.size())
                .mapToObj(i -> runAsync(
                        () -> doJobWithLock(clients.get(i), "[worker#" + i + "] ")
                )).collect(toList());

        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
                .join();
    }

    private static void doJobWithLock(HazelcastInstance hazelcastInstance, String prefix) {
        var lock = hazelcastInstance.getCPSubsystem().getLock(LOCK_NAME);

        for (;;)
        {
            lock.lock();
            System.out.println(prefix + "Got lock for async job");
            try {
                System.out.println(prefix + "In lock. Do some job...");
                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                System.out.println(prefix + "Finished async job");
                lock.unlock();
            }
        }

    }

    private static ClientConfig getClientConfig(String port) {
        var config = new ClientConfig();

        var address = "localhost:" + port;
        config.getNetworkConfig()
                .addAddress(address);

        return config;
    }

}
