package kr.hhplus.be.server.product.usecase.integration;


import kr.hhplus.be.server.common.constant.RedisKey;
import kr.hhplus.be.server.product.usecase.ClearProductSalesCacheUseCase;
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
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DisplayName("상품 관련 테스트")
public class ClearProductSalesCacheUseCaseTest {
    
    @Autowired
    private ClearProductSalesCacheUseCase clearProductSalesCacheUseCase;

    @Autowired
    private RedisTemplate<String, Long> redis;

    @Test
    void 인기상품캐시삭제() {
        // given
        String zsetKey = RedisKey.Product.productSalesKey(LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(4).toString());
        redis.opsForZSet().add(zsetKey, 1L, 2L);

        // when
        clearProductSalesCacheUseCase.execute();

        // then
        Boolean salesExists = redis.hasKey(zsetKey);
        assertThat(salesExists).isFalse();
    }
}
