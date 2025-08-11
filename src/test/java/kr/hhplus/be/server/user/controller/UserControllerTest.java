package kr.hhplus.be.server.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.coupon.step.CouponStep;
import kr.hhplus.be.server.order.step.OrderStep;
import kr.hhplus.be.server.payment.step.PaymentStep;
import kr.hhplus.be.server.product.step.ProductStep;
import kr.hhplus.be.server.user.infrastructure.jpa.JpaUserRepository;
import kr.hhplus.be.server.user.step.UserStep;
import kr.hhplus.be.server.user.usecase.dto.UserRequestDTO;
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
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.utility.TestcontainersConfiguration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@DisplayName("유저 관련 테스트")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate ;

    @BeforeEach
    void setUp() {
        clearTestData();
        initTestData();
    }

    private void clearTestData() {
        jdbcTemplate.execute("TRUNCATE TABLE users;");
    }

    private void initTestData() {
        jpaUserRepository.save(UserStep.유저엔티티_기본값());
    }


    @Nested
    @DisplayName("유저 포인트 충전 성공 케이스")
    class success{

        @Test
        @DisplayName("요청 데이터가 정상적일 경우 포인트를 충전한다.")
        void 포인트충전() throws Exception {
            // given
            UserRequestDTO request = UserStep.유저요청생성_기본값();

            // when
            ResultActions result = UserStep.유저포인트충전요청(mockMvc, objectMapper, request);

            // then
            result.andExpect(jsonPath("$.status").value(201));
        }
    }

    @Nested
    @DisplayName("유저 포인트 충전 실패 케이스")
    class fail{

        @Test
        @DisplayName("존재하지 않는 유저일 경우 UserNotFoundException이 발생한다.")
        void 포인트충전() throws Exception {
            // given
            UserRequestDTO request = UserStep.유저요청생성_기본값();
            request.setUserId(0L);

            // when
            ResultActions result = UserStep.유저포인트충전요청(mockMvc, objectMapper, request);

            // then
            result.andExpect(jsonPath("$.code").value("UserNotFound"));
        }
    }
}