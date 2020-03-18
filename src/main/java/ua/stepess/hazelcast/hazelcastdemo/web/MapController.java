package ua.stepess.hazelcast.hazelcastdemo.web;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping(MapController.CONTROLLER_BASE_URL)
public class MapController {

    public static final String CONTROLLER_BASE_URL = "/api/v1/map";

    private final HazelcastInstance hazelcastInstance;

    public MapController(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @PostMapping("/{key}")
    public ResponseEntity<String> write(@PathVariable String key,
                                        @RequestBody String content,
                                        UriComponentsBuilder uriBuilder) {
        hazelcastInstance.getMap("data")
                .set(key, content);

        var uri = uriBuilder.path(CONTROLLER_BASE_URL + "/{key}")
                .build(key);

        return created(uri)
                .body(content);
    }

    @GetMapping("/{key}")
    public String read(@PathVariable String key) {
        return hazelcastInstance.<String, String>getMap("data")
                .get(key);
    }

    @GetMapping
    public Map<String, String> readAll() {
        return hazelcastInstance.getMap("data");
    }
}
