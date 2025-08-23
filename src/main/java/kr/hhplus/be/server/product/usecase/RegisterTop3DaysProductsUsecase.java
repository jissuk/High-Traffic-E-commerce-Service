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
        String salesKeyD1 = PRODUCT_SALES_PREFIX + today.minusDays(1);
        String salesKeyD2 = PRODUCT_SALES_PREFIX + today.minusDays(2);
        String salesKeyD3 = PRODUCT_SALES_PREFIX + today.minusDays(3);

        redis.execute((RedisCallback<Void>) connection -> {
            connection.zUnionStore(
                    PRODUCT_SALES_3DAYS_TOTAL.getBytes(),
                    Aggregate.SUM,
                    Weights.of(1, 1, 1),
                    salesKeyD1.getBytes(),
                    salesKeyD2.getBytes(),
                    salesKeyD3.getBytes()
            );
            return null;
        });
    }
}
