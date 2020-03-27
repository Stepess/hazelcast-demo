package ua.stepess.hazelcast.hazelcastdemo;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.stream.Collectors.toList;

public class Counter {

    private final static String NONE_COUNTER_KEY = "none";
    private final static String PESSIMISTIC_COUNTER_KEY = "pessimistic";
    private final static String OPTIMISTIC_COUNTER_KEY = "optimistic";


    public static void main(String[] args) {

        var ports = List.of("5701", "5702", "5703");

        var clients = ports.stream()
                .map(Counter::getClientConfig)
                .map(HazelcastClient::newHazelcastClient)
                .collect(toList());

        var lockMode = OPTIMISTIC_COUNTER_KEY;

        var hzClient = clients.get(0);

        IMap<String, Integer> map = hzClient.getMap(lockMode);
        map.put(lockMode, 0);

        var futures = clients.stream()
                .map(client -> parallelCount(client, 1000, lockMode))
                .collect(toList());

        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
                .join();

        map = hzClient.getMap(lockMode);
        Integer result = map.get(lockMode);

        System.out.println("Result: " + result );

    }

    private static ClientConfig getClientConfig(String port) {
        var config = new ClientConfig();

        var address = "localhost:" + port;
        config.getNetworkConfig()
                .addAddress(address);

        return config;
    }

    public static CompletableFuture<Void> parallelCount(HazelcastInstance hazelcastInstance, Integer end, String lockMode) {
        IMap<String, Integer> map = hazelcastInstance.getMap(lockMode);

        var job = constructJob(lockMode, map, end);

        return runAsync(job);
    }

    private static Runnable constructJob(@RequestParam String lock, IMap<String, Integer> map, @PathVariable Integer end) {
        switch (lock) {
            case NONE_COUNTER_KEY:
                return () -> countWithoutLock(map, lock, end);
            case PESSIMISTIC_COUNTER_KEY:
                return () -> countWithPessimisticLock(map, lock, end);
            case OPTIMISTIC_COUNTER_KEY:
                return () -> countWithOptimisticLock(map, lock, end);
            default:
                return () -> countWithoutLock(map, NONE_COUNTER_KEY, end);
        }
    }

    private static void countWithoutLock(Map<String, Integer> map, String key, int to) {
        int value;

        for (int i = 0; i < to; i++) {
            value = map.get(key);
            map.put(key, ++value);
        }
    }

    private static void countWithPessimisticLock(IMap<String, Integer> map, String key, int to) {
        int value;

        for (int i = 0; i < to; i++) {
            map.lock(key);
            try {
                value = map.get(key);
                map.put(key, ++value);
            } finally {
                map.unlock(key);
            }
        }
    }

    private static void countWithOptimisticLock(IMap<String, Integer> map, String key, int to) {
        int oldValue;
        int newValue;

        for (int i = 0; i < to; i++) {
            do {
                oldValue = map.get(key);
                newValue = oldValue + 1;
            } while (!replace(map, key, oldValue, newValue));
        }
    }


    private static boolean replace(IMap<String, Integer> map, String key, Integer oldValue, Integer newValue) {
        Integer actualValue = map.get(key);
        map.lock(key);

        try {
            if (oldValue.equals(actualValue)) {
                map.put(key, newValue);
                return true;
            }
        } finally {
            map.unlock(key);
        }

        return false;
    }
}
