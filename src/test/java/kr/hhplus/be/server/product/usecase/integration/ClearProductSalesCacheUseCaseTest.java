package kr.hhplus.be.server.product.usecase.integration;


import kr.hhplus.be.server.product.usecase.ClearProductSalesCacheUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DisplayName("상품 관련 테스트")
public class ClearProductSalesCacheUseCaseTest {
    
    @Autowired
    private ClearProductSalesCacheUseCase clearProductSalesCacheUseCase;

    @Autowired
    private RedisTemplate<String, Object> redis;

    @Test
    void 인기상품캐시삭제() {

        // when
        clearProductSalesCacheUseCase.execute();

        // then
        String hashKey = "PopularProduct";
        String zsetKey = "sales:" + LocalDate.now().minusDays(4);

        Boolean productExists = redis.hasKey(hashKey);
        Boolean salesExists = redis.hasKey(zsetKey);

        // given
        assertAll(
            ()-> assertThat(productExists).isFalse(),
            ()-> assertThat(salesExists).isFalse()
        );

    }
}
