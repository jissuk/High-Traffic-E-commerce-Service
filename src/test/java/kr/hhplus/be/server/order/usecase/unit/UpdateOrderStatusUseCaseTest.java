//package kr.hhplus.be.server.order.usecase.unit;
//
//import kr.hhplus.be.server.common.sender.OrderDataSender;
//import kr.hhplus.be.server.order.domain.mapper.OrderItemMapper;
//import kr.hhplus.be.server.order.domain.mapper.OrderMapper;
//import kr.hhplus.be.server.order.domain.model.Order;
//import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
//import kr.hhplus.be.server.order.domain.repository.OrderRepository;
//import kr.hhplus.be.server.order.exception.OrderNotFoundException;
//import kr.hhplus.be.server.order.step.OrderStep;
//import kr.hhplus.be.server.order.usecase.reader.OrderItemReader;
//import kr.hhplus.be.server.order.usecase.reader.OrderReader;
//import kr.hhplus.be.server.payment.step.PaymentStep;
//import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@DisplayName("주문 상태 수정 테스트")
//@ExtendWith(MockitoExtension.class)
//public class UpdateOrderStatusUseCaseTest {
//    @InjectMocks
//    UpdateOrderStatusUseCase updateOrderStatusUseCase;
//    @Mock
//    private OrderReader orderReader;
//    @Mock
//    private OrderItemReader orderItemReader;
//    @Mock
//    private OrderRepository orderRepositroy;
//    @Mock
//    OrderItemRepository orderItemRepository;
//    @Mock
//    OrderDataSender orderDataSender;
//
//    @Spy
//    private OrderMapper orderMapper;
//    @Spy
//    private OrderItemMapper orderItemMapper;
//
//    @Nested
//    @DisplayName("주문 상태 수정 성공 케이스")
//    class success{
//
//        @Test
//        @DisplayName("주문과 주문상세가 존재할 경우 주문 상태를 수정한다.")
//        void 주문상태수정(){
//            // given
//            PaymentCommand command = PaymentStep.결제커맨드_기본값();
//            when(orderReader.findOrderOrThrow(command.orderId())).thenReturn(OrderStep.주문_기본값());
//            when(orderItemReader.findOrderItemOrThrow(command.orderItemId())).thenReturn(OrderStep.주문상세_기본값());
//
//            // when
//            updateOrderStatusUseCase.execute(command);
//
//            // then
//            verify(orderRepositroy).save(any(Order.class));
//
//            // 외부 데이터 플랫폼
////            verify(orderDataSender).send(any(OrderItem.class));
//        }
//    }
//
//    @Nested
//    @DisplayName("주문 상태 수정 실패 케이스")
//    class fail{
//
//        @Test
//        @DisplayName("존재하지 않는 주문일 경우 OrderNotFoundException이 발생한다.")
//        void 주문상태수정_존재하지않는_주문일_경우(){
//            // given
//            PaymentCommand command = PaymentStep.결제커맨드_기본값();
//            when(orderReader.findOrderOrThrow(command.orderId())).thenThrow(new OrderNotFoundException());
//
//            // when & then
//            assertThatThrownBy(() -> updateOrderStatusUseCase.execute(command))
//                    .isInstanceOf(OrderNotFoundException.class);
//        }
//    }
//}
