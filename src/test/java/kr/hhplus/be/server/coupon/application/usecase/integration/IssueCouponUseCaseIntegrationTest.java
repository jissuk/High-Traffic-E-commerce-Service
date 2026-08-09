package kr.hhplus.be.server.coupon.application.usecase.integration;

import kr.hhplus.be.server.common.outbox.scheduler.OutboxRelayScheduler;
import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.exception.DuplicateCouponIssueException;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaCouponRepository;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaUserCouponRepository;
import kr.hhplus.be.server.coupon.application.usecase.IssueCouponUseCase;
import kr.hhplus.be.server.coupon.application.command.UserCouponCommand;
import kr.hhplus.be.server.coupon.infrastructure.redis.keys.CouponRedisKey;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.infrastructure.jpa.JpaUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.awaitility.Awaitility.await;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(TestcontainersConfiguration.class)
@DisplayName("쿠폰 관련 통합 테스트")
public class IssueCouponUseCaseIntegrationTest {

    @Autowired private IssueCouponUseCase issueCouponUseCase;
    @Autowired private OutboxRelayScheduler outboxRelayScheduler;
    @Autowired private RedisTemplate<String, Long> redis;
    @Autowired private JpaUserRepository userRepository;
    @Autowired private JpaCouponRepository couponRepository;
    @Autowired private JpaUserCouponRepository userCouponRepository;

    private List<User> savedUser;
    private Coupon savedCoupon;

    @BeforeEach
    void setUp() {
        clearTestDBData();
        clearTestRedisData();
        initTestDBData();
        initTestRedisData();
    }

    @Nested
    @DisplayName("성공 케이스")
    class success {

        @Test
        @DisplayName("실시간 쿠폰 발급 Kafka 비동기 처리")
        void 쿠폰발급요청시_비동기적으로_유저쿠폰이_생성된다(){
            // given
            long userId = savedUser.get(0).getId();
            long couponId = savedCoupon.getId();
            UserCouponCommand command = new UserCouponCommand(userId, couponId);
            String quantityKey = CouponRedisKey.getQuantityKey(savedCoupon.getId());
            long remainingCoupons = 9L;

            // when
            issueCouponUseCase.execute(command);
            outboxRelayScheduler.relay();

            // then
            Long couponQuantity = redis.opsForValue().get(quantityKey);

            await()
                    .atMost(Duration.ofSeconds(5))
                    .untilAsserted(() -> {
                        assertThat(couponQuantity).isEqualTo(remainingCoupons);
                        assertThat(userCouponRepository.findByCouponIdAndUserId(couponId, userId));
                    });
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class fail {
        @Test
        @DisplayName("실시간 쿠폰 발급 동일 유저 발급")
        void 실시간쿠폰발급_동일유저발급(){
            // given
            long userId = savedUser.get(0).getId();
            long couponId = savedCoupon.getId();
            UserCouponCommand request = new UserCouponCommand(userId, couponId);
            issueCouponUseCase.execute(request);

            // when & then
            assertThatThrownBy(() -> issueCouponUseCase.execute(request))
                    .isInstanceOf(DuplicateCouponIssueException.class);
        }
    }

    private void clearTestDBData(){
        userRepository.deleteAll();
        couponRepository.deleteAll();
        userCouponRepository.deleteAll();
        savedUser = new ArrayList<>();
        savedCoupon = null;
    }

    private void clearTestRedisData(){
        redis.getConnectionFactory()
                .getConnection()
                .serverCommands()
                .flushDb();
    }
    private void initTestDBData() {
        for (int i = 1; i <= 10; i++) {
            User user = User.builder()
                    .point(40000L)
                    .build();
            savedUser.add(userRepository.save(user));
        }

        long couponDiscount = 3000L;
        long couponQuantity = 500L;
        String couponDescription = "여름 특별 할인 쿠폰";
        LocalDateTime expiration = LocalDateTime.now().plusMonths(3);
        Coupon coupon = Coupon.builder()
                .discount(couponDiscount)
                .quantity(couponQuantity)
                .description(couponDescription)
                .expiredAt(expiration)
                .build();
        savedCoupon = couponRepository.save(coupon);
    }

    private void initTestRedisData(){
        long couponId = savedCoupon.getId();
        long quantity = 10L;
        String quantityKey = CouponRedisKey.getQuantityKey(couponId);
        String issuedKey = CouponRedisKey.getIssuedKey(couponId);
        redis.opsForValue().set(quantityKey , quantity);
        redis.opsForValue().setBit(issuedKey, 0, false);
    }
}
