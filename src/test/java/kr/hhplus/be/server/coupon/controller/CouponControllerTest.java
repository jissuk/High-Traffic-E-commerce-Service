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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.utility.TestcontainersConfiguration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
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

    @BeforeEach
    void setUp() {
        clearTestData();
        initTestData();
    }

    private void clearTestData() {
        jdbcTemplate.execute("TRUNCATE TABLE users;");
        jdbcTemplate.execute("TRUNCATE TABLE coupons;");
    }

    private void initTestData() {
        jpaUserRepository.save(UserStep.유저엔티티_기본값());
        jpaCouponRepository.save(CouponStep.쿠폰엔티티_기본값());
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

    @Nested
    @DisplayName("선착순 쿠폰 실패 케이스")
    class fail{
        @Test
        @DisplayName("존재하지 않는 유저일 경우 UserNotFoundException이 발생한다.")
        void 선착순쿠폰발급_존재하지않는_유저일_경우() throws Exception {
            // given
            UserCouponRequest request = CouponStep.유저쿠폰요청_유저ID지정(2L);

            // when
            ResultActions result = CouponStep.선착순쿠폰발급요청(mockMvc, objectMapper, request);

            // then
            result.andExpect(jsonPath("$.code").value("UserNotFound"));
        }

        @Test
        @DisplayName("존재하지 않는 쿠폰일 경우 CouponNotFoundException이 발생한다.")
        void 선착순쿠폰발급_존재하지않는_쿠폰일_경우() throws Exception {
            // given
            UserCouponRequest request = CouponStep.유저쿠폰요청_쿠폰ID지정(2L);
            // when
            ResultActions result = CouponStep.선착순쿠폰발급요청(mockMvc, objectMapper, request);

            // then
            result.andExpect(jsonPath("$.code").value("CouponNotFound"));
        }

    }
}
