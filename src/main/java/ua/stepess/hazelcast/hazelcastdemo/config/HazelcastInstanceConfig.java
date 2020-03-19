package ua.stepess.hazelcast.hazelcastdemo.config;

import com.hazelcast.config.Config;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ua.stepess.hazelcast.hazelcastdemo.config.properties.HazelcastProperties;

@Profile("hz-instance")
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(HazelcastProperties.class)
public class HazelcastInstanceConfig {

    private final HazelcastProperties properties;

    @Bean
    public Config instanceConfig() {
        var config = new Config("hazelcast-worker");

        config.getNetworkConfig()
                .setPort(properties.getPort());

        return config;
    }

}
