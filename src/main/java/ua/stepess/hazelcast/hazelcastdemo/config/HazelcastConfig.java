package ua.stepess.hazelcast.hazelcastdemo.config;

import com.hazelcast.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {

    @Bean
    public Config instanceConfig() {
        return new Config("hazelcast-worker");
    }

}
