package kr.hhplus.be.server.config.toss;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "toss")
public record TossProperties(
    @NotBlank
    String secretApiKey,

    @NotBlank
    String approveUrl
) {
}
