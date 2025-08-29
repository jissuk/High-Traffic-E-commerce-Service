package kr.hhplus.be.server.config.redis;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "spring.data.redis")
public record RedisProperties(

    @NotBlank
    String host,

    @Positive
    int port
) {}


