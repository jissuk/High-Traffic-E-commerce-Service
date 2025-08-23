package kr.hhplus.be.server.coupon.usecase.integration;

import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.coupon.domain.repository.UserCouponRepository;
import kr.hhplus.be.server.coupon.step.CouponStep;
import kr.hhplus.be.server.coupon.usecase.CouponQueueUseCase;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.step.UserStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.testcontainers.utility.TestcontainersConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(TestcontainersConfiguration.class)
@DisplayName("쿠폰 관련 통합 테스트")
public class CouponQueueUseCaseTest {

    @Autowired
    private CouponQueueUseCase queueUseCase;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private StringRedisTemplate couponRedis;

    public static final String COUPON_ISSUE_QUEUE = "coupon:issue:queue";

    @BeforeEach
    void setUp() {
        initTestDBData();
        initTestRedisData();
    }

    void initTestDBData(){
        userRepository.save(UserStep.유저_기본값());
        couponRepository.save(CouponStep.쿠폰_기본값());
    }

    void initTestRedisData(){
        long userId = 1L;
        long couponId = 1L;
        String value = userId + ":" + couponId;

        couponRedis.opsForZSet().add(COUPON_ISSUE_QUEUE, value, System.currentTimeMillis());
    }

    @Test
    void 비동기유저요청캐시조회및삭제() {

        // when
        queueUseCase.execute();

        // then
        assertAll(
            ()->  assertThat(couponRedis.opsForZSet().range(COUPON_ISSUE_QUEUE, 0, 0))
                    .as("캐시 삭제 확인")
                    .hasSize(0),
            ()-> assertThat(userCouponRepository.findById(1L))
                    .as("유저 쿠폰 생성 확인")
                    .isNotNull()
        );
    }
}
