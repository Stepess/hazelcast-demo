package ua.stepess.hazelcast.hazelcastdemo.web;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.concurrent.CompletableFuture.runAsync;

@Slf4j
@RestController
@RequestMapping("api/v1/count")
public class ParallelCounterController {

    private final String NONE_COUNTER_KEY = "none";
    private final String PESSIMISTIC_COUNTER_KEY = "pessimistic";
    private final String OPTIMISTIC_COUNTER_KEY = "optimistic";

    private final HazelcastInstance hazelcastInstance;

    public ParallelCounterController(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @GetMapping("/{end}")
    public String parallelCount(@PathVariable Integer end, @RequestParam Integer workers, @RequestParam("lock") String lockMode) {
        IMap<String, Integer> map = hazelcastInstance.getMap("count");

        log.info("Started counting: number of workers [{}], count end [{}], lock mode [{}]", workers, end, lockMode);

        int initialValue = 0;
        map.put(lockMode, initialValue);

        var job = constructJob(lockMode, map, initialValue, end);

        var futures = supplyToWorkers(workers, job);

        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
                .join();

        Integer result = map.get(lockMode);
        log.info("Finished counting: result [{}]", result);

        return "{ \"result\":" + result + "}";

    }

    private List<CompletableFuture<Void>> supplyToWorkers(@RequestParam Integer workers, Runnable job) {
        ExecutorService executor = Executors.newFixedThreadPool(workers);

        return IntStream.range(0, workers)
                .mapToObj(i -> runAsync(job, executor))
                .collect(Collectors.toList());
    }

    private Runnable constructJob(@RequestParam String lock, IMap<String, Integer> map, int initialValue, @PathVariable Integer end) {
        switch (lock) {
            case NONE_COUNTER_KEY:
                return () -> countWithoutLock(map, lock, initialValue, end);
            case PESSIMISTIC_COUNTER_KEY:
                return () -> countWithPessimisticLock(map, lock, initialValue, end);
            case OPTIMISTIC_COUNTER_KEY:
                return () -> countWithOptimisticLock(map, lock, initialValue, end);
            default:
                return () -> countWithoutLock(map, NONE_COUNTER_KEY, initialValue, end);
        }
    }

    private void countWithoutLock(Map<String, Integer> map, String key, int from, int to) {
        int value;

        for (int i = from; i < to; i++) {
            value = map.get(key);
            map.put(key, ++value);
        }
    }

    private void countWithPessimisticLock(IMap<String, Integer> map, String key, int from, int to) {
        int value;

        for (int i = from; i < to; i++) {
            map.lock(key);
            try {
                value = map.get(key);
                map.put(key, ++value);
            } finally {
                map.unlock(key);
            }
        }
    }

    private void countWithOptimisticLock(IMap<String, Integer> map, String key, int from, int to) {
        int oldValue;
        int newValue;

        for (int i = from; i < to; i++) {
            do {
                oldValue = map.get(key);
                newValue = oldValue + 1;
            } while (!map.replace(key, oldValue, newValue));
        }
    }
}
