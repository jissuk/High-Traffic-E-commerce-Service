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
@DisplayName("주문 관련 Repository 테스트")
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepositroy;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        clearTestData();
    }
    private void clearTestData() {
        jdbcTemplate.execute("TRUNCATE TABLE tbl_order;");
    }

    @Nested
    @DisplayName("성공 케이스")
    class success {

        @Test
        void 주문_등록() {
            // given
            OrderEntity entity = OrderStep.주문엔티티_기본값();

            // when
            OrderEntity saved = orderRepositroy.save(entity);

            // then
            assertThat(saved).isNotNull();

        }

        @Test
        void 주문_조회() {
            // given
            OrderEntity saved = orderRepositroy.save(OrderStep.주문엔티티_기본값());

            // when
            OrderEntity found = orderRepositroy.findById(saved.getId());

            // then
            assertThat(found).isEqualTo(saved);
        }
    }
}
