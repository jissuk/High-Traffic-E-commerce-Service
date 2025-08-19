package kr.hhplus.be.server.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.order.step.OrderStep;
import kr.hhplus.be.server.order.usecase.dto.OrderItemRequest;
import kr.hhplus.be.server.product.infrastructure.jpa.JpaProductRepository;
import kr.hhplus.be.server.product.step.ProductStep;
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
@DisplayName("주문 관련 테스트")
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private JpaProductRepository jpaProductRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        clearTestData();
        initTestData();
    }

    private void clearTestData() {
        jdbcTemplate.execute("TRUNCATE TABLE users;");
        jdbcTemplate.execute("TRUNCATE TABLE products;");
        jdbcTemplate.execute("TRUNCATE TABLE orders;");
        jdbcTemplate.execute("TRUNCATE TABLE order_items;");
    }

    private void initTestData() {
        jpaUserRepository.save(UserStep.유저엔티티_기본값());
        jpaProductRepository.save(ProductStep.상품엔티티_기본값());
    }


    @Nested
    @DisplayName("주문 성공 케이스")
    class success{

        @Test
        @DisplayName("요청 데이터가 정상적일 경우 주문을 등록한다.")
        void 주문() throws Exception {
            // given
            OrderItemRequest request = OrderStep.주문상세요청_기본값();

            // when
            ResultActions result = OrderStep.주문요청(mockMvc, objectMapper, request);

            // then
            result.andExpect(status().isOk());
        }

    }
    @Nested
    @DisplayName("주문 실패 케이스")
    class fail{

        @Test
        @DisplayName("존재하지 않는 유저일 경우 UserNotFoundException이 발생한다.")
        void 주문_존재하지않는_유저일_경우() throws Exception {
            // given
            OrderItemRequest request = OrderStep.주문상세요청_유저ID지정(2L);

            // when
            ResultActions result = OrderStep.주문요청(mockMvc, objectMapper, request);

            // then
            result.andExpect(jsonPath("$.code").value("UserNotFound"));
        }

        @Test
        @DisplayName("존재하지 않는 상품일 경우 ProductNotFoundException이 발생한다.")
        void 주문_존재하지않는_상품일_경우() throws Exception {
            // given
            OrderItemRequest request = OrderStep.주문상세요청_상품ID지정(2L);

            // when
            ResultActions result = OrderStep.주문요청(mockMvc, objectMapper, request);

            // then
            result.andExpect(jsonPath("$.code").value("ProductNotFound"));
        }
    }
}
