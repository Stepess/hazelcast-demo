package ua.stepess.hazelcast.hazelcastdemo.config;

import com.hazelcast.client.config.ClientConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.stepess.hazelcast.hazelcastdemo.config.properties.HazelcastProperties;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(HazelcastProperties.class)
public class HazelcastClientConfig {

    private static final String HOST = "localhost";
    private static final String COLON = ":";

    private final HazelcastProperties properties;

    @Bean
    public ClientConfig clientConfig() {
        var config = new ClientConfig();

        var address = HOST + COLON + properties.getPort();
        config.getNetworkConfig()
               .addAddress(address);
        return config;
    }


}
