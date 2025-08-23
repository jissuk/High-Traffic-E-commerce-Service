package kr.hhplus.be.server.product.controller;

import kr.hhplus.be.server.product.infrastructure.jpa.JpaProductRepository;
import kr.hhplus.be.server.product.step.ProductStep;
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


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@DisplayName("상품 관련 테스트")
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JpaProductRepository jpaProductRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() throws Exception {
        clearTestData();
        initTestData();
    }


    private void clearTestData() {
        // MySql
        jdbcTemplate.execute("TRUNCATE TABLE products;");

    }
    private void initTestData() throws Exception {
    // MySql
        jpaProductRepository.save(ProductStep.상품엔티티_기본값());
    }

    @Nested
    @DisplayName("상품 조회 성공 케이스")
    class success{

        @Test
        @DisplayName("요청 데이터가 정상적일 경우 단건의 상품을 조회한다.")
        void 상품조회() throws Exception {
            // given
            long productId = 1L;
            // when
            ResultActions result = ProductStep.단건상품조회요청(mockMvc, productId);

            // then
            result.andExpect(status().isOk());
        }

        @Test
        @DisplayName("전체 상품을 조회한다.")
        void 전체상품조회() throws Exception {
            // given

            // when
            ResultActions result = ProductStep.전체상품조회요청(mockMvc);

            // then
            result.andExpect(status().isOk());
        }

        @Test
        @DisplayName("인기 판매 상품을 조회한다.")
        void 인기판매상품조회() throws Exception {
            // given

            // when
            ResultActions result = ProductStep.인기판매상품조회요청(mockMvc);

            // then
            result.andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("상품 조회 실패 케이스")
    class fail{

        @Test
        @DisplayName("존재하지 않는 상품일 경우 ProductNotFoundException이 발생한다.")
        void 상품조회_존재하지않는_상품일_경우() throws Exception {
            // given
            long productId = 0L;

            // when
            ResultActions result = ProductStep.단건상품조회요청(mockMvc, productId);

            // then
            result.andExpect(jsonPath("$.code").value("ProductNotFound"));
        }
    }
}
