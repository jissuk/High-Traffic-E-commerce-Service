//package kr.hhplus.be.server.order.usecase;
//
//
//import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
//import kr.hhplus.be.server.order.domain.repository.OrderRepository;
//import kr.hhplus.be.server.order.step.OrderStep;
//import kr.hhplus.be.server.order.usecase.command.OrderCommand;
//import kr.hhplus.be.server.payment.domain.Repository.PaymentRepository;
//import kr.hhplus.be.server.product.domain.repository.ProductRepository;
//import kr.hhplus.be.server.product.step.ProductStep;
//import kr.hhplus.be.server.user.domain.repository.UserRepository;
//import kr.hhplus.be.server.user.step.UserStep;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.testcontainers.utility.TestcontainersConfiguration;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertAll;
//
//@SpringBootTest
//@DisplayName("주문 관련 테스트")
//@Import(TestcontainersConfiguration.class)
//public class RegisterOrderUseCaseTest {
//
//    @Autowired
//    private RegisterOrderUseCase registerOrderUseCase;
//
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private PaymentRepository jpaPaymentRepository;
//    @Autowired
//    private OrderRepository jpaOrderRepository;
//    @Autowired
//    private OrderItemRepository jpaOrderItemRepository;
//
//    @BeforeEach
//    void setUp() {
//        userRepository.save(UserStep.defualtUser());
//        productRepository.save(ProductStep.defaultProduct()); // quantity : 5
//    }
//
//    @Nested
//    @DisplayName("성공 케이스")
//    class success{
//        @Test
//        void 주문() {
//            // given
//            OrderCommand request = OrderStep.defaultOrderCommand(); // quantity : 2
//
//            // when
//            registerOrderUseCase.execute(request);
//
//            // then
//            long createdId = 1L;
//            assertAll(
//                ()-> assertThat(productRepository.findById(request.productId()).getQuantity())
//                        .as("남은 상품 수량 확인")
//                        .isEqualTo(3L),
//                ()-> assertThat(jpaOrderRepository.findById(createdId).getId())
//                        .as("주문 생성 확인")
//                        .isEqualTo(createdId),
//                ()-> assertThat(jpaOrderItemRepository.findById(createdId).getId())
//                        .as("주문 상세 생성 확인")
//                        .isEqualTo(createdId),
//                ()-> assertThat(jpaPaymentRepository.findById(createdId).getId())
//                        .as("결제 생성 확인")
//                        .isEqualTo(createdId)
//            );
//        }
//    }
//}
