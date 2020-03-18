package ua.stepess.hazelcast.hazelcastdemo.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Validated
@ConfigurationProperties("hazelcast")
public class HazelcastProperties {
    @Positive
    @NotNull
    private Integer port;
}
