package ua.stepess.hazelcast.hazelcastdemo.web;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/map")
public class MapController {

    private final HazelcastInstance hazelcastInstance;

    public MapController(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @PostMapping("/{key}")
    public String write(@PathVariable String key, @RequestBody String content) {
        var map = hazelcastInstance.getMap("data");
        map.set(key, content);
        return "putted";
    }

    @GetMapping("/{key}")
    public String read(@PathVariable String key) {
        var map = hazelcastInstance.<String, String>getMap("data");
        return map.get(key);
    }

    @GetMapping
    public Map<String, String> readAll() {
        return hazelcastInstance.<String, String>getMap("data");
    }
}
