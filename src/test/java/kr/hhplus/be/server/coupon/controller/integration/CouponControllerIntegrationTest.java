package kr.hhplus.be.server.coupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaCouponRepository;
import kr.hhplus.be.server.coupon.step.CouponStep;
import kr.hhplus.be.server.coupon.usecase.dto.UserCouponRequest;
import kr.hhplus.be.server.user.infrastructure.jpa.JpaUserRepository;
import kr.hhplus.be.server.user.step.UserStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.utility.TestcontainersConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@DisplayName("쿠폰 발급 통합 테스트")
class CouponControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private JpaCouponRepository jpaCouponRepository;
    @Autowired private JpaUserRepository jpaUserRepository;
    @Autowired private JdbcTemplate jdbcTemplate;
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
        UserCouponRequest request = CouponStep.defaultUserCouponRequest();

        // when
        ResultActions result = mockMvc.perform(post("/coupons/issue")
                                        .content(objectMapper.writeValueAsString(request))
                                        .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk());
    }

    private void clearTestData() {
        jdbcTemplate.execute("TRUNCATE TABLE users;");
        jdbcTemplate.execute("TRUNCATE TABLE coupons;");
    }

    private void initTestData() {
        jpaUserRepository.save(UserStep.defualtUserEntity());
        jpaCouponRepository.save(CouponStep.defaultCouponEntity());
    }

    private void initTestRedisData(){
        long couponId = 1L;
        long userId = 1L;

        redis.opsForValue().set(getQuantityKey(couponId), 10L);
        redis.opsForValue().setBit(getIssuedKey(userId), 0, false);
    }

    private String getQuantityKey(long couponId) {
        return "coupon:issue:" + couponId + ":quantity";
    }

    private String getIssuedKey(long userId) {
        return "coupon:issue:" + userId + ":issued";
    }
}