package kr.hhplus.be.server.coupon.application.usecase.concurrency;

import kr.hhplus.be.server.coupon.application.command.UserCouponCommand;
import kr.hhplus.be.server.coupon.application.usecase.IssueCouponUseCase;
import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaCouponRepository;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaUserCouponRepository;
import kr.hhplus.be.server.coupon.infrastructure.redis.CouponRedisKey;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.infrastructure.jpa.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(TestcontainersConfiguration.class)
@DisplayName("쿠폰 관련 동시성 테스트")
public class IssueCouponUseCaseConcurrencyTest {

    @Autowired
    private IssueCouponUseCase issueCouponUseCase;
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

    @Test
    @DisplayName("동시에 10건의 실시간쿠폰발급 요청을 보낼 경우 모두 성공")
    void 실시간쿠폰발급_분산락_동시성() throws Exception {
        // given
        long couponId = savedCoupon.getId();
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        List<Future<Void>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            int userIndex = i;
            futures.add(executor.submit(() -> {
                // when
                try {
                    long userId = savedUser.get(userIndex).getId();
                    UserCouponCommand command = new UserCouponCommand(userId, couponId);
                    issueCouponUseCase.execute(command);
                    return null;
                } finally {
                    latch.countDown();
                }
            }));
        }
        latch.await();
        executor.shutdown();

        AtomicLong successCount = new AtomicLong();
        AtomicLong failureCount = new AtomicLong();

        for (Future<Void> future : futures) {
            try {
                future.get();
                successCount.getAndIncrement();
            } catch (ExecutionException e) {
                if (e.getCause() instanceof ObjectOptimisticLockingFailureException) {
                    failureCount.getAndIncrement();
                } else{
                    e.printStackTrace();
                }
            }
        }

        // then
        String quantityKey = CouponRedisKey.getQuantityKey(couponId);
        Long couponQuantity = redis.opsForValue().get(quantityKey);

        assertAll(
                ()-> assertThat(couponQuantity)
                        .as("잔여 쿠폰 개수 확인")
                        .isEqualTo(0L),
                ()-> assertThat(successCount.get())
                        .as("성공한 요청 수")
                        .isEqualTo(10),
                ()-> assertThat(failureCount.get())
                        .as("실패한 요청 수")
                        .isEqualTo(0)
        );
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
