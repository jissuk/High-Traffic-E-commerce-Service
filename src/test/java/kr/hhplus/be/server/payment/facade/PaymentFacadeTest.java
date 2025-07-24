package kr.hhplus.be.server.payment.facade;

import kr.hhplus.be.server.coupon.domain.mapper.UserCouponMapper;
import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.domain.repository.OrderRepositroy;
import kr.hhplus.be.server.order.step.OrderStep;
import kr.hhplus.be.server.payment.domain.Repository.PaymentRepository;
import kr.hhplus.be.server.payment.step.PaymentStep;
import kr.hhplus.be.server.payment.usecase.dto.PaymentRequestDTO;
import kr.hhplus.be.server.product.domain.model.ProductEntity;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.step.ProductStep;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.step.UserStep;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@DisplayName("결제 테스트")
public class PaymentFacadeTest {

    @Autowired
    private PaymentFacade paymentFacade;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderRepositroy orderRepositroy  ;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PaymentRepository paymentRepository;



    @BeforeEach
    void setUp() {
        clearRepositories();
        initTestData();
    }

    private void clearRepositories() {
        userRepository.clear();
        productRepository.clear();
        orderItemRepository.clear();
        orderRepositroy.clear();
        paymentRepository.clear();
    }

    private void initTestData() {
        userRepository.save(UserStep.기본유저엔티티생성());
        productRepository.save(ProductStep.기본상품엔티티생성());
        orderItemRepository.save(OrderStep.기본주문상세엔티티생성());
        orderRepositroy.save(OrderStep.기본주문엔티티생성());
        paymentRepository.save(PaymentStep.기본결제엔티티생성());
    }

    @Nested
    @DisplayName("성공 케이스")
    class success{

        @Test
        void 결제() {
            // given
            PaymentRequestDTO request = PaymentStep.기본결제요청생성();

            // when & then
            assertDoesNotThrow(() -> paymentFacade.requestPayment(request));
        }
    }
}
