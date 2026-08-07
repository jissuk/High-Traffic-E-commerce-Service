package kr.hhplus.be.server.product.application.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.product.application.port.out.PopularProductPort;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.zset.Aggregate;
import org.springframework.data.redis.connection.zset.Weights;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;
import java.time.ZoneId;

@UseCase
@RequiredArgsConstructor
public class RegisterTop3DaysProductsUseCase {

    private final PopularProductPort port;

    public void execute() {

        port.refreshPopularProductRanking();
    }
}
