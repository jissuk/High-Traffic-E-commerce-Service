package kr.hhplus.be.server.product.presentation;

import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.infrastructure.jpa.JpaProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.utility.TestcontainersConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@DisplayName("상품 관련 테스트")
public class ProductControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private JpaProductRepository productRepository;

    private Product savedProduct;

    @BeforeEach
    void setUp() throws Exception {
        clearTestData();
        initTestData();
    }

    @Nested
    @DisplayName("상품 조회 성공 케이스")
    class success{

        @Test
        @DisplayName("요청 데이터가 정상적일 경우 단건의 상품을 조회한다.")
        void 상품조회() throws Exception {
            // given
            long productId = savedProduct.getId();
            // when
            ResultActions result = mockMvc.perform(get("/products" + "/{productId}", productId)
                                            .contentType(MediaType.APPLICATION_JSON))
                                            .andDo(print());
            // then
            result.andExpect(status().isOk());
        }

        @Test
        @DisplayName("전체 상품을 조회한다.")
        void 전체상품조회() throws Exception {
            // given

            // when
            ResultActions result = mockMvc.perform(get("/products")
                                            .contentType(MediaType.APPLICATION_JSON))
                                            .andDo(print());

            // then
            result.andExpect(status().isOk());
        }

//        @Test
//        @DisplayName("인기 판매 상품을 조회한다.")
//        void 인기판매상품조회() throws Exception {
//            // given
//
//            // when
//            ResultActions result = mockMvc.perform(get("/products/popular")
//                                            .contentType(MediaType.APPLICATION_JSON))
//                                            .andDo(print());
//
//            // then
//            result.andExpect(status().isOk());
//        }
    }

    @Nested
    @DisplayName("상품 조회 실패 케이스")
    class fail{

        @Test
        @DisplayName("존재하지 않는 상품일 경우 ProductNotFoundException이 발생한다.")
        void 상품조회_존재하지않는_상품일_경우() throws Exception {
            // given
            long productId = Integer.MAX_VALUE;

            // when
            ResultActions result = mockMvc.perform(get("/products" + "/{productId}", productId)
                                            .contentType(MediaType.APPLICATION_JSON))
                                            .andDo(print());

            // then
            result.andExpect(jsonPath("$.code").value("ProductNotFound"));
        }
    }

    private void clearTestData() {
        productRepository.deleteAll();
    }

    private void initTestData(){
        Product product = Product.builder()
                .productName("기본 상품")
                .price(2000L)
                .quantity(200L)
                .build();
        savedProduct = productRepository.save(product);
    }
}
