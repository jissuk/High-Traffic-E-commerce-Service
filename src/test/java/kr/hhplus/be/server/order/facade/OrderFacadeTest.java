package kr.hhplus.be.server.order.facade;

import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.step.OrderStep;
import kr.hhplus.be.server.order.usecase.dto.OrderItemRequestDTO;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.step.ProductStep;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.step.UserStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@DisplayName("주문 테스트")
public class OrderFacadeTest {

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @BeforeEach
    void setUp() {
        clearRepositories();
        initTestData();
    }

    private void clearRepositories() {
        userRepository.clear();
        productRepository.clear();
        orderItemRepository.clear();
    }

    private void initTestData() {
        userRepository.save(UserStep.기본유저엔티티생성());
        productRepository.save(ProductStep.기본상품엔티티생성());
        orderItemRepository.save(OrderStep.기본주문상세엔티티생성());
    }

    @Nested
    @DisplayName("성공 케이스")
    class success{
        @Test
        void 주문() {
            // given
            OrderItemRequestDTO request = OrderStep.기본주문상세요청생성();

            // when & then
            assertDoesNotThrow(() -> orderFacade.createOrder(request));
        }
    }

}
