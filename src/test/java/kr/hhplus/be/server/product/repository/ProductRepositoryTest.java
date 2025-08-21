package kr.hhplus.be.server.product.repository;

import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderItemRepository;
import kr.hhplus.be.server.order.step.OrderStep;
import kr.hhplus.be.server.payment.domain.model.PaymentEntity;
import kr.hhplus.be.server.payment.infrastructure.jpa.JpaPaymentRepository;
import kr.hhplus.be.server.payment.step.PaymentStep;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.domain.model.ProductEntity;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.infrastructure.jpa.JpaProductRepository;
import kr.hhplus.be.server.product.step.ProductStep;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DisplayName("상품 관련 Repository 테스트")
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JpaPaymentRepository jpaPaymentRepository;
    @Autowired
    private JpaOrderItemRepository jpaOrderItemRepository;
    @Autowired
    private JpaProductRepository jpaProductRepository;

    @BeforeEach
    void setUp() {
        initDBTestDBData();
    }

    void initDBTestDBData(){
        ProductEntity product1 = jpaProductRepository.save(ProductStep.상품엔티티_기본값());
        ProductEntity product2 = jpaProductRepository.save(ProductStep.상품엔티티_기본값());
        ProductEntity product3 = jpaProductRepository.save(ProductStep.상품엔티티_기본값());
        ProductEntity product4 = jpaProductRepository.save(ProductStep.상품엔티티_기본값());

        // 예상 스코어
        // product1 : 3
        // product2 : 4
        // product3 : 1
        // product4 : 5
        OrderItemEntity orderItem1 = OrderStep.주문상세엔티티_기본값_상품ID_수량_지정(product1.getId(), 3L);
        OrderItemEntity orderItem2 = OrderStep.주문상세엔티티_기본값_상품ID_수량_지정(product2.getId(), 2L);
        OrderItemEntity orderItem3 = OrderStep.주문상세엔티티_기본값_상품ID_수량_지정(product3.getId(), 1L);
        OrderItemEntity orderItem4 = OrderStep.주문상세엔티티_기본값_상품ID_수량_지정(product2.getId(), 2L);
        OrderItemEntity orderItem5 = OrderStep.주문상세엔티티_기본값_상품ID_수량_지정(product1.getId(), 3L);
        OrderItemEntity orderItem6 = OrderStep.주문상세엔티티_기본값_상품ID_수량_지정(product4.getId(), 5L);

        jpaOrderItemRepository.save(orderItem1);
        jpaOrderItemRepository.save(orderItem2);
        jpaOrderItemRepository.save(orderItem3);
        jpaOrderItemRepository.save(orderItem4);
        jpaOrderItemRepository.save(orderItem5);
        jpaOrderItemRepository.save(orderItem6);

        LocalDateTime nowDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        PaymentEntity payment1 = PaymentStep.결제엔티티_기본값_주문상세ID_생성일_성공상태_지정(orderItem1.getId(), nowDateTime.minusDays(2));
        PaymentEntity payment2 = PaymentStep.결제엔티티_기본값_주문상세ID_생성일_성공상태_지정(orderItem2.getId(), nowDateTime.minusDays(1));
        PaymentEntity payment3 = PaymentStep.결제엔티티_기본값_주문상세ID_생성일_성공상태_지정(orderItem3.getId(), nowDateTime.minusDays(3));
        PaymentEntity payment4 = PaymentStep.결제엔티티_기본값_주문상세ID_생성일_성공상태_지정(orderItem4.getId(), nowDateTime.minusDays(3));
        // 카운트 x
        PaymentEntity payment5 = PaymentStep.결제엔티티_기본값_주문상세ID_생성일_성공상태_지정(orderItem5.getId(), nowDateTime.minusDays(4));
        PaymentEntity payment6 = PaymentStep.결제엔티티_기본값_주문상세ID_생성일_성공상태_지정(orderItem6.getId(), nowDateTime.minusDays(1));

        jpaPaymentRepository.save(payment1);
        jpaPaymentRepository.save(payment2);
        jpaPaymentRepository.save(payment3);
        jpaPaymentRepository.save(payment4);
        jpaPaymentRepository.save(payment5);
        jpaPaymentRepository.save(payment6);

        // 예상 순위 인기 상품 3개
        // product4 -> product2 -> product1 -> product3(탈락)
    }
    @Nested
    @DisplayName("성공 케이스")
    class success{

        @Test
        @DisplayName("최근 3일 판매량이 높은 상위 3개의 제품을 조회한다.")
        void 인기판매상품조회() {

            // when
            List<Product> result = productRepository.findPopularProduct3Days();

            // then
            assertAll(
                    ()-> assertThat(result).hasSize(3),
                    ()-> assertThat(result.get(0).getId()).isEqualTo(4L),
                    ()-> assertThat(result.get(1).getId()).isEqualTo(2L),
                    ()-> assertThat(result.get(2).getId()).isEqualTo(1L)
            );
        }
    }
}
