package kr.hhplus.be.server.order.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.order.step.OrderStep;
import kr.hhplus.be.server.order.usecase.dto.OrderRequest;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.infrastructure.jpa.JpaProductRepository;
import kr.hhplus.be.server.product.step.ProductStep;
import kr.hhplus.be.server.user.domain.model.User;
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
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.utility.TestcontainersConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@DisplayName("주문 관련 테스트")
public class OrderControllerIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private JpaUserRepository jpaUserRepository;
    @Autowired private JpaProductRepository jpaProductRepository;
    @Autowired private JdbcTemplate jdbcTemplate;

    private User savedUser;
    private Product savedProduct;

    @BeforeEach
    void setUp() {
        clearTestData();
        initTestData();
    }

    @Nested
    @DisplayName("주문 성공 케이스")
    class success{
        @Test
        @DisplayName("요청 데이터가 정상적일 경우 주문을 등록한다.")
        void 주문() throws Exception {
            // given
            OrderRequest request = OrderRequest.builder()
                                                .userId(1L)
                                                .productId(1L)
                                                .quantity(1L)
                                                .build();

            // when
            ResultActions result = mockMvc.perform(post("/orders")
                                            .content(objectMapper.writeValueAsString(request))
                                            .contentType(MediaType.APPLICATION_JSON))
                                        .andDo(print());

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
            OrderRequest request = OrderRequest.builder()
                    .userId(2L)
                    .productId(1L)
                    .quantity(1L)
                    .build();

            // when
            ResultActions result = mockMvc.perform(post("/orders")
                                            .content(objectMapper.writeValueAsString(request))
                                            .contentType(MediaType.APPLICATION_JSON))
                                        .andDo(print());

            // then (return 값 형태 확인해보기)
            result.andExpect(jsonPath("$.code").value("UserNotFound"));
        }

        @Test
        @DisplayName("존재하지 않는 상품일 경우 ProductNotFoundException이 발생한다.")
        void 주문_존재하지않는_상품일_경우() throws Exception {
            // given
            OrderRequest request = OrderRequest.builder()
                    .userId(2L)
                    .productId(1L)
                    .quantity(1L)
                    .build();

            // when
            ResultActions result = mockMvc.perform(post("/orders")
                                            .content(objectMapper.writeValueAsString(request))
                                            .contentType(MediaType.APPLICATION_JSON))
                                        .andDo(print());

            // then
            result.andExpect(jsonPath("$.code").value("ProductNotFound"));
        }
    }

    private void clearTestData() {
        jdbcTemplate.execute("TRUNCATE TABLE users;");
        jdbcTemplate.execute("TRUNCATE TABLE products;");
        jdbcTemplate.execute("TRUNCATE TABLE orders;");
        jdbcTemplate.execute("TRUNCATE TABLE order_items;");
        savedUser = null;
        savedProduct = null;
    }

    private void initTestData() {
        User user = User.builder()
                        .point(40000L)
                        .build();
        savedUser = jpaUserRepository.save(user);

        Product product = Product.builder()
                                .productName("기본 상품")
                                .price(2000L)
                                .quantity(200L)
                                .build();
        savedProduct = jpaProductRepository.save(product);
    }
}
