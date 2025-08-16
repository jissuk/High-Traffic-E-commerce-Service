package kr.hhplus.be.server.product.usecase.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderItemRepository;
import kr.hhplus.be.server.order.step.OrderStep;
import kr.hhplus.be.server.payment.domain.model.PaymentEntity;
import kr.hhplus.be.server.payment.infrastructure.jpa.JpaPaymentRepository;
import kr.hhplus.be.server.payment.step.PaymentStep;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.domain.model.ProductEntity;
import kr.hhplus.be.server.product.infrastructure.jpa.JpaProductRepository;
import kr.hhplus.be.server.product.step.ProductStep;
import kr.hhplus.be.server.product.usecase.GetPopularProductUseCase;
import kr.hhplus.be.server.product.usecase.dto.ProductResponseDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DisplayName("상품 관련 테스트")
public class GetPopularProductUseCaseTest {

    @Autowired
    private GetPopularProductUseCase getPopularProductUseCase;

    @Autowired
    private RedisTemplate<String, Object> redis;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JpaPaymentRepository jpaPaymentRepository;
    @Autowired
    private JpaOrderItemRepository jpaOrderItemRepository;
    @Autowired
    private JpaProductRepository jpaProductRepository;


    void clearTestDBData() {
        jdbcTemplate.execute("TRUNCATE TABLE products;");
        jdbcTemplate.execute("TRUNCATE TABLE order_items;");
        jdbcTemplate.execute("TRUNCATE TABLE payments;");
    }

    private void clearTestRedisData() {
        // Redis
        Set<String> keys = redis.keys("*");
        if (keys != null && !keys.isEmpty()) {
            redis.delete(keys);
        }
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

    void initTestRedisData() throws Exception {
        Product product1 = ProductStep.상품_기본값_ID지정(1);
        Product product2 = ProductStep.상품_기본값_ID지정(2);
        Product product3 = ProductStep.상품_기본값_ID지정(3);
        Product product4 = ProductStep.상품_기본값_ID지정(4);

        int orderQuantity1 = 5;
        int orderQuantity2 = 3;
        int orderQuantity3 = 2;
        int orderQuantity4 = 1;

        // product1 : 예상 스코어 5
        redis.opsForZSet().incrementScore("sales:" + LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1),
                                                                            product1.getId(),
                                                                            orderQuantity1);
        /* 4일전 => 집계되지 않음 */
        redis.opsForZSet().incrementScore("sales:" + LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(4),
                                                                            product1.getId(),
                                                                            orderQuantity1);
        redis.opsForHash().put("PopularProduct", product1.getId(), objectMapper.writeValueAsString(product1));

        // product2 : 예상 스코어 6
        for(int i = 1; i < 3; i++){
            redis.opsForZSet().incrementScore("sales:" + LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(i),
                                                                                product2.getId(),
                                                                                orderQuantity2);
            redis.opsForHash().put("PopularProduct", product2.getId(), objectMapper.writeValueAsString(product2));
        }
        // product3 : 예상 스코어 4
        for(int i = 2; i < 4; i++){
            redis.opsForZSet().incrementScore("sales:" + LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(i),
                                                                                product3.getId(),
                                                                                orderQuantity3);
            redis.opsForHash().put("PopularProduct", product3.getId(), objectMapper.writeValueAsString(product3));
        }
        // product4 : 예상 스코어 1
        redis.opsForZSet().incrementScore("sales:" + LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(2),product4.getId(),orderQuantity4);
        redis.opsForHash().put("PopularProduct", product4.getId(), objectMapper.writeValueAsString(product4));

        // 예상 순위 인기 상품 3개
        // product2 -> product1 -> product3 -> product4(탈락)
    }

    @Nested
    @DisplayName("인기 판매 상품 조회 성공 케이스")
    class success{

        @Test
        @DisplayName("최근 3일 판매량이 높은 상위 3개의 제품을 Redis의 캐시에서 조회한다.")
        void 인기판매상품조회_Redis() throws Exception {
            // given
            clearTestRedisData();
            initTestRedisData();

            // when
            List<ProductResponseDTO> result = getPopularProductUseCase.execute();

            // then
            assertThat(result).hasSize(3);
            assertThat(result.get(0).getId()).isEqualTo(2L);
            assertThat(result.get(1).getId()).isEqualTo(1L);
            assertThat(result.get(2).getId()).isEqualTo(3L);
        }

        @Test
        @DisplayName("최근 3일 판매량이 높은 상위 3개의 제품을 Mysql에서 조회한다.")
        void 인기판매상품조회_Mysql() throws Exception {
            // given
            clearTestDBData();
            clearTestRedisData();
            initDBTestDBData();

            // when
            List<ProductResponseDTO> result = getPopularProductUseCase.execute();
            for(ProductResponseDTO productResponseDTO : result){
                System.out.println(productResponseDTO);
            }

            // then
            assertThat(result).hasSize(3);
            assertThat(result.get(0).getId()).isEqualTo(4L);
            assertThat(result.get(1).getId()).isEqualTo(2L);
            assertThat(result.get(2).getId()).isEqualTo(1L);
        }
    }
}
