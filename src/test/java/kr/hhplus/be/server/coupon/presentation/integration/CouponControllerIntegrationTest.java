package kr.hhplus.be.server.coupon.presentation.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaCouponRepository;
import kr.hhplus.be.server.coupon.presentation.dto.UserCouponRequest;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.infrastructure.jpa.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@DisplayName("쿠폰 발급 통합 테스트")
class CouponControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private JpaCouponRepository jpaCouponRepository;
    @Autowired private JpaUserRepository jpaUserRepository;
    @Autowired private RedisTemplate<String, Long> redis;

    @BeforeEach
    void setUpIntegrationTestData() {
        clearTestData();
        initTestData();
        initTestRedisData();
    }

    @Test
    @DisplayName("쿠폰 수량이 남아있으면 발급 성공")
    void issueCoupon_success() throws Exception {
        // given
        Long userId = 1L;
        Long couponId = 1L;
        UserCouponRequest request = UserCouponRequest.builder()
                                                    .userId(userId)
                                                    .couponId(couponId)
                                                    .build();

        // when
        ResultActions result = mockMvc.perform(post("/coupons/issue")
                                        .content(objectMapper.writeValueAsString(request))
                                        .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk());
    }

    private void clearTestData() {
        jpaUserRepository.deleteAll();
        jpaCouponRepository.deleteAll();
    }

    private void initTestData() {
        long point = 40000L;
        User user = User.builder()
                .point(point)
                .build();

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

        jpaUserRepository.save(user);
        jpaCouponRepository.save(coupon);
    }

    private void initTestRedisData(){
        long couponId = 1L;

        redis.opsForValue().set(getQuantityKey(couponId), 10L);
        redis.opsForValue().setBit(getIssuedKey(couponId), 0, false);
    }

    private String getQuantityKey(long couponId) {
        return "coupon:issue:" + couponId + ":quantity";
    }

    private String getIssuedKey(long couponId) {
        return "coupon:issue:" + couponId + ":issued";
    }
}