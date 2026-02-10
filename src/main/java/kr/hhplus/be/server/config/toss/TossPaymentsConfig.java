package kr.hhplus.be.server.config.toss;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "toss")
// yml파일 toss로 만들기
public record TossPaymentsConfig(
    @NotBlank
    String approveUrl,

    @NotBlank
    String refundUrl,

    @NotBlank
    String secretApiKey,

    @NotBlank
    String secureKey
) {
}
