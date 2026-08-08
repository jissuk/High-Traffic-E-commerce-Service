package kr.hhplus.be.server.product.infrastructure.redis.adapter;

import kr.hhplus.be.server.product.application.port.out.PopularProductPort;
import kr.hhplus.be.server.product.infrastructure.redis.keys.ProductRedisKey;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.zset.Aggregate;
import org.springframework.data.redis.connection.zset.Weights;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class PopularProductAdapter implements PopularProductPort {

    private final RedisTemplate<String, Long> redis;

    public static final String PRODUCT_SALES_PREFIX = "product:sales:";

    @Override
    public void refreshPopularProductRanking() {

        aggregateLast3DaysSales();
        publishPopularProductEvictEvent();
    }

    private void aggregateLast3DaysSales() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));

        String salesProductKeyD1 = PRODUCT_SALES_PREFIX + today.minusDays(1);
        String salesProductKeyD2 = PRODUCT_SALES_PREFIX + today.minusDays(2);
        String salesProductKeyD3 = PRODUCT_SALES_PREFIX + today.minusDays(3);

        long dailyWeight = 1;

        redis.execute((RedisCallback<Void>) connection -> {
            connection.zUnionStore(
                    ProductRedisKey.PRODUCT_SALES_3DAYS_TOTAL.getBytes(),
                    Aggregate.SUM,
                    Weights.of(dailyWeight, dailyWeight, dailyWeight),
                    salesProductKeyD1.getBytes(),
                    salesProductKeyD2.getBytes(),
                    salesProductKeyD3.getBytes()
            );
            return null;
        });
    }

    private void publishPopularProductEvictEvent() {
        redis.convertAndSend(ProductRedisKey.POPULAR_PRODUCT_EVICT, ProductRedisKey.PRODUCT_SALES_3DAYS_TOTAL);
    }
}