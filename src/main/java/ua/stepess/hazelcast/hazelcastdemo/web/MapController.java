package ua.stepess.hazelcast.hazelcastdemo.web;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/v1/hazelcast")
public class MapController {

    private final HazelcastInstance hazelcastInstance;

    public MapController(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @PostMapping("/{key}")
    public String write(@PathVariable String key,
                        @RequestBody String content) {
        var map = hazelcastInstance.getMap("data");
        map.set(key, content);
        return "putted";
    }
}
