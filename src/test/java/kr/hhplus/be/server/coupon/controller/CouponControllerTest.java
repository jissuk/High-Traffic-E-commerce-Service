package kr.hhplus.be.server.coupon.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.common.constant.RedisKey;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.utility.TestcontainersConfiguration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@DisplayName("쿠폰 관련 테스트")
public class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JpaCouponRepository jpaCouponRepository;
    @Autowired
    private JpaUserRepository jpaUserRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisTemplate<String, Long> redis;

    @BeforeEach
    void setUp() {
        clearTestData();
        initTestData();
        initTestRedisData();
    }

    private void clearTestData() {
        jdbcTemplate.execute("TRUNCATE TABLE users;");
        jdbcTemplate.execute("TRUNCATE TABLE coupons;");
    }

    private void initTestData() {
        jpaUserRepository.save(UserStep.유저엔티티_기본값());
        jpaCouponRepository.save(CouponStep.쿠폰엔티티_기본값());
    }

    private void initTestRedisData(){
        long couponId = 1L;
        long userId = 1L;
        String quantityKey = RedisKey.Coupon.issueCouponQuantityKey(couponId);
        String issuedKey = RedisKey.Coupon.userCouponIssuedKey(userId, couponId);

        redis.opsForValue().set(quantityKey , 10L);
        redis.opsForValue().setBit(issuedKey, 0, false);
    }

    @Nested
    @DisplayName("선착순 쿠폰 성공 케이스")
    class success{

        @Test
        @DisplayName("요청 데이터가 정상적이며 쿠폰 수량이 남아있을 경우 쿠폰을 발급한다.")
        void 선착순쿠폰발급() throws Exception {
            // given
            UserCouponRequest request = CouponStep.유저쿠폰요청_기본값();
            // when
            ResultActions result = CouponStep.선착순쿠폰발급요청(mockMvc, objectMapper, request);

            // then
            result.andExpect(status().isOk());
        }
    }
}
