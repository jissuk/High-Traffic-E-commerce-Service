package kr.hhplus.be.server.coupon.infrastructure.redis.integration;

import kr.hhplus.be.server.config.redis.RedisProperties;
import kr.hhplus.be.server.config.redis.SpringRedisConfig;
import kr.hhplus.be.server.coupon.application.command.UserCouponCommand;
import kr.hhplus.be.server.coupon.infrastructure.redis.CouponRedisKey;
import kr.hhplus.be.server.coupon.infrastructure.redis.IssueCouponAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataRedisTest
@Import({
        SpringRedisConfig.class,
        IssueCouponAdapter.class
})
@EnableConfigurationProperties(RedisProperties.class)
public class IssueCouponAdapterSliceTest {

    @Autowired private RedisTemplate<String, Long> redis;
    @Autowired private IssueCouponAdapter issueCouponAdapter;

    @BeforeEach
    void setup() {
        clearTestData();
    }

    @Nested
    @DisplayName("성공 케이스")
    class success{
        @Test
        @DisplayName("쿠폰을 발급받지 않은 사용자는 발급 처리된다.")
        void 쿠폰중복발급검증(){
            // given
            long userId = 1L;
            long couponId = 1L;
            UserCouponCommand command = UserCouponCommand.builder()
                                                            .userId(userId)
                                                            .couponId(couponId)
                                                            .build();
            String issuedKey = CouponRedisKey.getIssuedKey(couponId);
            redis.opsForValue().setBit(issuedKey, userId, false);

            // when
            issueCouponAdapter.validateDuplicateIssue(command);

            // then
            Boolean result = redis.opsForValue().getBit(issuedKey, userId);
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("쿠폰 수량이 남아있을 경우 쿠폰의 수량이 차감된다.")
        void 쿠폰수량차감(){
            // given
            long userId = 1L;
            long couponId = 1L;
            UserCouponCommand command = UserCouponCommand.builder()
                                                            .userId(userId)
                                                            .couponId(couponId)
                                                            .build();
            long quantity = 10L;
            long remainingCoupons = 9L;
            String quantityKey = CouponRedisKey.getQuantityKey(couponId);
            redis.opsForValue().set(quantityKey , quantity);

            // when
            issueCouponAdapter.decrementQuantity(command);

            // then
            Long result = redis.opsForValue().get(quantityKey);
            assertThat(result).isEqualTo(remainingCoupons);
        }
    }

    private void clearTestData() {
        redis.getConnectionFactory()
                .getConnection()
                .serverCommands()
                .flushDb();
    }
}