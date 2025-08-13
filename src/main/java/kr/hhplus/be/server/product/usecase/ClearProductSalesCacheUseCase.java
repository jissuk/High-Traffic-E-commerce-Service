package kr.hhplus.be.server.product.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;


@UseCase
@RequiredArgsConstructor
public class ClearProductSalesCacheUseCase {

    private final RedisTemplate<String, Object> redisTemplate;

    public void execute() {
        String key = "sales:" + LocalDate.now().minusDays(4);

        redisTemplate.delete(key);

    }
}
