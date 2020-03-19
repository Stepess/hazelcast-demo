package ua.stepess.hazelcast.hazelcastdemo.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.stepess.hazelcast.hazelcastdemo.config.properties.HazelcastProperties;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(HazelcastProperties.class)
public class HazelcastClientConfig {

    private final HazelcastProperties properties;

    @Bean
    public ClientConfig clientConfig() {
        var config = new ClientConfig();

        var address = "http://172.19.0.3:" + properties.getPort();
        config.getNetworkConfig()
               .addAddress(address);

        return config;
    }

    @Bean

    public HazelcastInstance instance(ClientConfig clientConfig) {
        return HazelcastClient.newHazelcastClient(clientConfig);
    }

}
