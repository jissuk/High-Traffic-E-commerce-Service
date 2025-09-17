package kr.hhplus.be.server.coupon.usecase.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.hhplus.be.server.common.outbox.scheduler.OutboxRelayScheduler;
import kr.hhplus.be.server.coupon.exception.DuplicateCouponIssueException;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaCouponRepository;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaUserCouponRepository;
import kr.hhplus.be.server.coupon.step.CouponStep;
import kr.hhplus.be.server.coupon.usecase.IssueCouponUseCase;
import kr.hhplus.be.server.coupon.usecase.command.UserCouponCommand;
import kr.hhplus.be.server.user.infrastructure.jpa.JpaUserRepository;
import kr.hhplus.be.server.user.step.UserStep;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(TestcontainersConfiguration.class)
@DisplayName("쿠폰 관련 통합 테스트")
public class IssueCouponUseCaseTest {
    
    @Autowired
    private IssueCouponUseCase issueCouponUseCase;
    @Autowired
    private OutboxRelayScheduler outboxRelayScheduler;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RedisTemplate<String, Long> redis;
    @Autowired
    private JpaUserRepository jpaUserRepository;
    @Autowired
    private JpaCouponRepository jpaCouponRepository;
    @Autowired
    private JpaUserCouponRepository jpaUserCouponRepository;

    public static final String COUPON_ISSUE_PREFIX = "coupon:issue:";
    public static final String ISSUED_SUFFIX = ":issued";
    public static final String QUANTITY_SUFFIX = ":quantity";
    @BeforeEach
    void setUp() {
        clearTestDBData();
        clearTestRedisData();
        initTestDBData();
        initTestRedisData();
    }
    private void clearTestDBData(){
        jdbcTemplate.execute("TRUNCATE TABLE users;");
        jdbcTemplate.execute("TRUNCATE TABLE coupons;");
        jdbcTemplate.execute("TRUNCATE TABLE user_coupons;");
    }

    private void clearTestRedisData(){
        Set<String> keys = redis.keys("*");
        if (keys != null && !keys.isEmpty()) {
            redis.delete(keys);
        }
    }
    private void initTestDBData() {
        for (int i = 1; i <= 10; i++) {
            jpaUserRepository.save(UserStep.유저엔티티_기본값());
        }
        jpaCouponRepository.save(CouponStep.쿠폰엔티티_기본값());
    }

    private void initTestRedisData(){
        long couponId = 1L;
        long userId = 1L;
        String quantityKey = COUPON_ISSUE_PREFIX +couponId + QUANTITY_SUFFIX;
        String issuedKey = COUPON_ISSUE_PREFIX + userId + ISSUED_SUFFIX;

        redis.opsForValue().set(quantityKey , 10L);
        redis.opsForValue().setBit(issuedKey, 0, false);
    }


    @Nested
    @DisplayName("성공 케이스")
    class success {

        @Test
        @DisplayName("실시간 쿠폰 발급 Kafka 비동기 처리")
        void 실시간쿠폰발급_비동기() throws JsonProcessingException, InterruptedException {
            // given
            long couponId = 1L;
            long userCouponId = 1L;
            long remainingCoupons = 9L;
            UserCouponCommand request = CouponStep.유저쿠폰커맨드_기본값();
            String quantityKey = COUPON_ISSUE_PREFIX +couponId + QUANTITY_SUFFIX;

            // when
            issueCouponUseCase.execute(request);
            outboxRelayScheduler.relayMessages();

            // then
            Long couponQuantity = redis.opsForValue().get(quantityKey);

            Thread.sleep(2000);

            assertAll(
                ()-> assertThat(couponQuantity)
                        .as("잔여 쿠폰 수")
                        .isEqualTo(remainingCoupons),
                ()-> assertThat(jpaUserCouponRepository.findById(userCouponId))
                        .as("유저 쿠폰 등록 확인")
                        .isPresent()
            );
        }

        @Test
        @DisplayName("동시에 10건의 실시간쿠폰발급 요청을 보낼 경우 모두 성공")
        void 실시간쿠폰발급_분산락_동시성() throws Exception {
            int threadCount = 10;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            List<Future<Void>> futures = new ArrayList<>();

            for (long i = 0; i < threadCount; i++) {
                long userId = i+1;
                futures.add(executor.submit(() -> {
                    try {
                        issueCouponUseCase.execute(CouponStep.유저쿠폰커맨드_유저ID지정(userId));
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
            long couponId = 1L;
            String quantityKey = COUPON_ISSUE_PREFIX +couponId + QUANTITY_SUFFIX;

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
    }

    @Nested
    @DisplayName("실패 케이스")
    class fail {
        @Test
        @DisplayName("실시간 쿠폰 발급 동일 유저 발급")
        void 실시간쿠폰발급_동일유저발급() throws JsonProcessingException {
            // given
            UserCouponCommand request = CouponStep.유저쿠폰커맨드_기본값();
            issueCouponUseCase.execute(request);

            // when & then
            assertThatThrownBy(() -> issueCouponUseCase.execute(request))
                    .isInstanceOf(DuplicateCouponIssueException.class);
        }
    }
}
