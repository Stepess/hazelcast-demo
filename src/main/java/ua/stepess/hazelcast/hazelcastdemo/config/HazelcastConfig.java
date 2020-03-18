package ua.stepess.hazelcast.hazelcastdemo.config;

import com.hazelcast.config.Config;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.stepess.hazelcast.hazelcastdemo.config.properties.HazelcastProperties;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(HazelcastProperties.class)
public class HazelcastConfig {

    private final HazelcastProperties properties;

    @Bean
    public Config instanceConfig() {
        var config = new Config("hazelcast-worker");

        config.getNetworkConfig()
                .setPort(properties.getPort());

        return config;
    }

}
