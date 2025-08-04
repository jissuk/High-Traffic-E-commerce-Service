package kr.hhplus.be.server.order.repository;

import kr.hhplus.be.server.order.domain.model.OrderEntity;
import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.order.step.OrderStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.utility.TestcontainersConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Import(TestcontainersConfiguration.class)
@DisplayName("주문 상세 관련 Repository 테스트")
public class OrderItemRepositoryTest {

    @Autowired
    private OrderRepository orderRepositroy;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        clearTestData();
    }

    private void clearTestData() {
        jdbcTemplate.execute("TRUNCATE TABLE tbl_orders;");
        jdbcTemplate.execute("TRUNCATE TABLE tbl_order_items;");
    }

    @Nested
    @DisplayName("성공 케이스")
    class success{
        @Test
        void 주문상세_등록() {
            // given
            OrderEntity order = orderRepositroy.save(OrderStep.주문엔티티_기본값());

            // when
            OrderItemEntity saved = orderItemRepository.save(OrderStep.주문상세엔티티_기본값(order));

            // then
            assertThat(saved).isNotNull();
        }

        @Test
        void 주문상세_조회() {
            // given
            OrderEntity order = orderRepositroy.save(OrderStep.주문엔티티_기본값());
            OrderItemEntity saved = orderItemRepository.save(OrderStep.주문상세엔티티_기본값(order));

            // when
            OrderItemEntity found = orderItemRepository.findById(saved.getId()).get();

            // then
            assertThat(found).isEqualTo(saved);
        }
    }
}
