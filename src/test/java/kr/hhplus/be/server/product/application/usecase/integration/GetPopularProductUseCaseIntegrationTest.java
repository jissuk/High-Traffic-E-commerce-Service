package kr.hhplus.be.server.product.application.usecase.integration;

import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderItemRepository;
import kr.hhplus.be.server.order.step.OrderStep;
import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.infrastructure.jpa.JpaPaymentRepository;
import kr.hhplus.be.server.payment.step.PaymentStep;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.infrastructure.jpa.JpaProductRepository;
import kr.hhplus.be.server.product.step.ProductStep;
import kr.hhplus.be.server.product.application.usecase.GetPopularProductUseCase;
import kr.hhplus.be.server.product.application.usecase.RegisterTop3DaysProductsUseCase;
import kr.hhplus.be.server.product.presentation.dto.ProductResponse;
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
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(TestcontainersConfiguration.class)
@DisplayName("상품 관련 테스트")
public class
GetPopularProductUseCaseIntegrationTest {

    @Autowired private GetPopularProductUseCase getPopularProductUseCase;
    @Autowired private RegisterTop3DaysProductsUseCase registerTop3DaysProductsUsecase;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private RedisTemplate<String, Long> redis;
    @Autowired private JpaPaymentRepository paymentRepository;
    @Autowired private JpaOrderItemRepository orderItemRepository;
    @Autowired private JpaProductRepository productRepository;

    public static final String PRODUCT_SALES_PREFIX = "product:sales:";

    @BeforeEach
    void setUp() {
        clearTestDBData();
        clearTestRedisData();
        initTestDBData();
    }

    @Nested
    @DisplayName("인기 판매 상품 조회 성공 케이스")
    class success{
        @Test
        @DisplayName("최근 3일 판매량이 높은 상위 3개의 제품을 Redis의 캐시에서 조회한다.")
        void 인기판매상품조회_Redis() throws Exception {
            // given
            initTestRedisData();
            registerTop3DaysProductsUsecase.execute();

            // when
            List<ProductResponse> result = getPopularProductUseCase.execute();

            // then
            assertAll(
                ()-> assertThat(result).as("조회 데이터 개수").hasSize(3),
                ()-> assertThat(result.get(0).id()).as("1순위").isEqualTo(2L),
                ()-> assertThat(result.get(1).id()).as("2순위").isEqualTo(1L),
                ()-> assertThat(result.get(2).id()).as("3순위").isEqualTo(3L)
            );
        }

//        @Test
//        @DisplayName("최근 3일 판매량이 높은 상위 3개의 제품을 Mysql에서 조회한다.")
//        void 인기판매상품조회_Mysql() throws Exception {
//            // given
//
//            // when
//            List<ProductResponse> result = getPopularProductUseCase.execute();
//            // then
//            assertAll(
//                ()-> assertThat(result).as("조회 데이터 개수").hasSize(3),
//                ()-> assertThat(result.get(0).id()).as("1순위").isEqualTo(4L),
//                ()-> assertThat(result.get(1).id()).as("2순위").isEqualTo(2L),
//                ()-> assertThat(result.get(2).id()).as("3순위").isEqualTo(1L)
//            );
//        }

    }

    private void clearTestRedisData() {
        redis.getConnectionFactory()
                .getConnection()
                .serverCommands()
                .flushDb();
    }

    void clearTestDBData() {
        jdbcTemplate.execute("TRUNCATE TABLE products;");
        jdbcTemplate.execute("TRUNCATE TABLE order_items;");
        jdbcTemplate.execute("TRUNCATE TABLE payments;");
    }

    void initTestDBData(){
        Product product1 = productRepository.save(ProductStep.productWithProductId(0));
        Product product2 = productRepository.save(ProductStep.productWithProductId(0));
        Product product3 = productRepository.save(ProductStep.productWithProductId(0));
        Product product4 = productRepository.save(ProductStep.productWithProductId(0));

        /**
         * 예상 스코어
         * product1 : 3
         * product2 : 4
         * product3 : 1
         * product4 : 5
         * */
        OrderItem orderItem1 = OrderStep.orderItemWithProductIdAndQuantity(product1.getId(), 3L);
        OrderItem orderItem2 = OrderStep.orderItemWithProductIdAndQuantity(product2.getId(), 2L);
        OrderItem orderItem3 = OrderStep.orderItemWithProductIdAndQuantity(product3.getId(), 1L);
        OrderItem orderItem4 = OrderStep.orderItemWithProductIdAndQuantity(product2.getId(), 2L);
        OrderItem orderItem5 = OrderStep.orderItemWithProductIdAndQuantity(product1.getId(), 3L);
        OrderItem orderItem6 = OrderStep.orderItemWithProductIdAndQuantity(product4.getId(), 5L);

        orderItemRepository.save(orderItem1);
        orderItemRepository.save(orderItem2);
        orderItemRepository.save(orderItem3);
        orderItemRepository.save(orderItem4);
        orderItemRepository.save(orderItem5);
        orderItemRepository.save(orderItem6);

        LocalDateTime nowDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        Payment payment1 = PaymentStep.paymentWithCreatedAt(nowDateTime.minusDays(2));
        Payment payment2 = PaymentStep.paymentWithCreatedAt(nowDateTime.minusDays(1));
        Payment payment3 = PaymentStep.paymentWithCreatedAt(nowDateTime.minusDays(3));
        Payment payment4 = PaymentStep.paymentWithCreatedAt(nowDateTime.minusDays(3));
        // 카운트 x
        Payment payment5 = PaymentStep.paymentWithCreatedAt(nowDateTime.minusDays(4));
        Payment payment6 = PaymentStep.paymentWithCreatedAt(nowDateTime.minusDays(1));

        paymentRepository.save(payment1);
        paymentRepository.save(payment2);
        paymentRepository.save(payment3);
        paymentRepository.save(payment4);
        paymentRepository.save(payment5);
        paymentRepository.save(payment6);

        /**
         * 인기 상품 조회 예상 순위(3개)
         * product4 -> product2 -> product1 -> product3(탈락)
         * */
    }

    void initTestRedisData(){
        Product product1 = ProductStep.productWithProductId(1);
        Product product2 = ProductStep.productWithProductId(2);
        Product product3 = ProductStep.productWithProductId(3);
        Product product4 = ProductStep.productWithProductId(4);

        LocalDate toDay = LocalDate.now();

        String salesKeyD1 = PRODUCT_SALES_PREFIX + toDay.minusDays(1);
        String salesKeyD2 = PRODUCT_SALES_PREFIX + toDay.minusDays(2);
        String salesKeyD3 = PRODUCT_SALES_PREFIX + toDay.minusDays(3);
        String salesKeyD4 = PRODUCT_SALES_PREFIX + toDay.minusDays(4);

        /*
         * product1 : 예상 스코어 5
         * product2 : 예상 스코어 6
         * product3 : 예상 스코어 4
         * product4 : 예상 스코어 1
         * */
        redis.opsForZSet().incrementScore(salesKeyD1, product1.getId(), 5);
        redis.opsForZSet().incrementScore(salesKeyD1, product2.getId(), 3);
        redis.opsForZSet().incrementScore(salesKeyD2, product2.getId(), 3);
        redis.opsForZSet().incrementScore(salesKeyD2, product3.getId(), 2);
        redis.opsForZSet().incrementScore(salesKeyD3, product3.getId(), 2);
        redis.opsForZSet().incrementScore(salesKeyD1,product4.getId(),1);

        // 4일전 => 집계되지 않음
        redis.opsForZSet().incrementScore(salesKeyD4, product1.getId(), 5);

        // 예상 순위 인기 상품 3개
        // product2 -> product1 -> product3 -> product4(탈락)
    }
}
