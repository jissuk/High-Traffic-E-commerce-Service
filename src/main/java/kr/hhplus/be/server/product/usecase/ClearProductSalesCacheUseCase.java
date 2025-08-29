package kr.hhplus.be.server.product.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;
import java.time.ZoneId;


@UseCase
@RequiredArgsConstructor
public class ClearProductSalesCacheUseCase {

    private final RedisTemplate<String, Long> redisTemplate;
    public static final String PRODUCT_SALES_PREFIX = "product:sales:";

    public void execute() {
        String zSetKey = PRODUCT_SALES_PREFIX + LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(4);
        redisTemplate.delete(zSetKey);
    }
}
