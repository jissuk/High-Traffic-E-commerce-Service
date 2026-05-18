package kr.hhplus.be.server.product.application.usecase.integration;

import kr.hhplus.be.server.product.application.usecase.ClearProductSalesCacheUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.testcontainers.utility.TestcontainersConfiguration;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DisplayName("상품 관련 테스트")
public class ClearProductSalesCacheUseCaseTest {
    
    @Autowired private ClearProductSalesCacheUseCase clearProductSalesCacheUseCase;
    @Autowired private RedisTemplate<String, Long> redis;

    @Test
    void 인기상품캐시삭제() {
        // given
        LocalDate expiredAt = LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(4);
        String zSetKey = getZSetKey(expiredAt);
        redis.opsForZSet().add(zSetKey, 1L, 2L);

        // when
        clearProductSalesCacheUseCase.execute();

        // then
        Boolean salesExists = redis.hasKey(zSetKey);
        assertThat(salesExists).isFalse();
    }

    private String getZSetKey(LocalDate date) {
        return "product:sales:" + date;
    }
}
