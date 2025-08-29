package kr.hhplus.be.server.product.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.zset.Aggregate;
import org.springframework.data.redis.connection.zset.Weights;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;
import java.time.ZoneId;

@UseCase
@RequiredArgsConstructor
public class RegisterTop3DaysProductsUsecase {

    private final RedisTemplate<String, Long> redis;

    public static final String PRODUCT_SALES_3DAYS_TOTAL  = "product:sales:3days:total";
    public static final String PRODUCT_SALES_PREFIX = "product:sales:";

    public void execute() {

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));

        String salesProductKeyD1 = PRODUCT_SALES_PREFIX + today.minusDays(1);
        String salesProductKeyD2 = PRODUCT_SALES_PREFIX + today.minusDays(2);
        String salesProductKeyD3 = PRODUCT_SALES_PREFIX + today.minusDays(3);

        long dailyWeight = 1;

        redis.execute((RedisCallback<Void>) connection -> {
            connection.zUnionStore(
                    PRODUCT_SALES_3DAYS_TOTAL.getBytes(),
                    Aggregate.SUM,
                    Weights.of(dailyWeight, dailyWeight, dailyWeight),
                    salesProductKeyD1.getBytes(),
                    salesProductKeyD2.getBytes(),
                    salesProductKeyD3.getBytes()
            );
            return null;
        });
    }
}
