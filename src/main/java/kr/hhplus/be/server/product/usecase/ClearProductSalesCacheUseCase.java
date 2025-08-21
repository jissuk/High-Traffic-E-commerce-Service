package kr.hhplus.be.server.product.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.common.constant.RedisKey;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;
import java.time.ZoneId;


@UseCase
@RequiredArgsConstructor
public class ClearProductSalesCacheUseCase {

    private final RedisTemplate<String, Long> redisTemplate;

    public void execute() {
        String zsetKey = RedisKey.Product.productSalesKey(LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(4).toString());
        redisTemplate.delete(zsetKey);
    }
}
