package kr.hhplus.be.server.product.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.common.constant.RedisKey;
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

    public void execute() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        String salesKeyD1 = RedisKey.Product.productSalesKey(today.minusDays(1).toString());
        String salesKeyD2 = RedisKey.Product.productSalesKey(today.minusDays(2).toString());
        String salesKeyD3 = RedisKey.Product.productSalesKey(today.minusDays(3).toString());

        String destKey = RedisKey.Product.PRODUCT_SALES_3DAYS_TOTAL;

        redis.execute((RedisCallback<Void>) connection -> {
            connection.zUnionStore(
                    destKey.getBytes(),
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
