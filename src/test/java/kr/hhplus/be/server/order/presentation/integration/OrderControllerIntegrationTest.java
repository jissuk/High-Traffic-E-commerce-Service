package kr.hhplus.be.server.order.presentation.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.CouponStatus;
import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaCouponRepository;
import kr.hhplus.be.server.coupon.infrastructure.jpa.JpaUserCouponRepository;
import kr.hhplus.be.server.order.presentation.dto.OrderRequest;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.infrastructure.jpa.JpaProductRepository;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.infrastructure.jpa.JpaUserRepository;
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

import java.time.LocalDateTime;

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
    @Autowired private JpaUserRepository userRepository;
    @Autowired private JpaCouponRepository couponRepository;
    @Autowired private JpaUserCouponRepository userCouponRepository;
    @Autowired private JpaProductRepository productRepository;

    private User savedUser;
    private Coupon savedCoupon;
    private UserCoupon savedUserCoupon;
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
            long userId = savedUser.getId();
            long productId = savedProduct.getId();
            long userCouponId = savedUserCoupon.getId();
            long quantity = 1L;
            long point = 3000L;
            OrderRequest request = OrderRequest.builder()
                                                .userId(userId)
                                                .productId(productId)
                                                .userCouponId(userCouponId)
                                                .quantity(quantity)
                                                .point(point)
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
            long userId = Integer.MAX_VALUE;
            long productId = savedProduct.getId();
            long quantity = 1L;
            long userCouponId = savedUserCoupon.getId();
            long point = 3000L;
            OrderRequest request = OrderRequest.builder()
                                                .userId(userId)
                                                .productId(productId)
                                                .userCouponId(userCouponId)
                                                .quantity(quantity)
                                                .point(point)
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
            long userId = savedUser.getId();
            long productId = Integer.MAX_VALUE;
            long quantity = 1L;
            long userCouponId = savedUserCoupon.getId();
            long point = 3000L;
            OrderRequest request = OrderRequest.builder()
                                                .userId(userId)
                                                .productId(productId)
                                                .userCouponId(userCouponId)
                                                .quantity(quantity)
                                                .point(point)
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
        userRepository.deleteAll();
        couponRepository.deleteAll();
        userCouponRepository.deleteAll();
        productRepository.deleteAll();

        savedUser = null;
        savedCoupon = null;
        savedUserCoupon = null;
        savedProduct = null;
    }

    private void initTestData() {
        User user = User.builder()
                .point(10_000L)
                .build();
        savedUser = userRepository.save(user);

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
        savedCoupon = couponRepository.save(coupon);

        UserCoupon userCoupon = UserCoupon.builder()
                .couponStatus(CouponStatus.ISSUED)
                .userId(savedUser.getId())
                .couponId(savedCoupon.getId())
                .build();
        savedUserCoupon = userCouponRepository.save(userCoupon);

        Product product = Product.builder()
                .productName("기본 상품")
                .price(2000L)
                .quantity(5L)
                .build();
        savedProduct = productRepository.save(product);
    }
}
