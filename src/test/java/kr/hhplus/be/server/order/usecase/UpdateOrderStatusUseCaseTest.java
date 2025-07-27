package kr.hhplus.be.server.order.usecase;

import kr.hhplus.be.server.common.sender.OrderDataSender;
import kr.hhplus.be.server.order.domain.mapper.OrderHistoryMapper;
import kr.hhplus.be.server.order.domain.mapper.OrderItemMapper;
import kr.hhplus.be.server.order.domain.mapper.OrderMapper;
import kr.hhplus.be.server.order.domain.model.OrderEntity;
import kr.hhplus.be.server.order.domain.model.OrderHistoryEntity;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import kr.hhplus.be.server.order.domain.repository.OrderHistoryRepository;
import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.domain.repository.OrderRepositroy;
import kr.hhplus.be.server.order.exception.OrderNotFoundException;
import kr.hhplus.be.server.order.step.OrderStep;
import kr.hhplus.be.server.payment.step.PaymentStep;
import kr.hhplus.be.server.payment.usecase.dto.PaymentRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.checkerframework.checker.signature.qual.DotSeparatedIdentifiers;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("주문 상태 수정 테스트")
@ExtendWith(MockitoExtension.class)
public class UpdateOrderStatusUseCaseTest {

    UpdateOrderStatusUseCase updateOrderStatusUseCase;

    @Mock
    private OrderRepositroy orderRepositroy;
    @Mock
    private OrderHistoryRepository orderHistoryRepository;
    @Mock
    OrderItemRepository orderItemRepository;
    @Mock
    OrderDataSender orderDataSender;

    @BeforeEach
    void setUp() {
        OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);
        OrderHistoryMapper orderHistoryMapper = Mappers.getMapper(OrderHistoryMapper.class);
        OrderItemMapper orderItemMapper = Mappers.getMapper(OrderItemMapper.class);
        updateOrderStatusUseCase = new UpdateOrderStatusUseCase(
                orderRepositroy,
                orderHistoryRepository,
                orderItemRepository,
                orderDataSender,
                orderMapper,
                orderHistoryMapper,
                orderItemMapper
        );
    }

    @Nested
    @DisplayName("주문 상태 수정 성공 케이스")
    class success{

        @Test
        @DisplayName("주문과 주문상세가 존재할 경우 주문 상태를 수정한다.")
        void 주문상태수정(){
            // given
            PaymentRequestDTO request = PaymentStep.기본결제요청생성();
            when(orderRepositroy.findById(request.getOrderId())).thenReturn(OrderStep.기본주문엔티티생성());
            when(orderItemRepository.findById(request.getOrderItemId())).thenReturn(OrderStep.기본주문상세엔티티생성());

            // when
            updateOrderStatusUseCase.execute(request);

            // then
            verify(orderRepositroy).update(any(OrderEntity.class));
            verify(orderHistoryRepository).save(any(OrderHistoryEntity.class));

            // 외부 데이터 플랫폼
//            verify(orderDataSender).send(any(OrderItem.class));
        }
    }

    @Nested
    @DisplayName("주문 상태 수정 실패 케이스")
    class fail{

        @Test
        @DisplayName("존재하지 않는 주문일 경우 OrderNotFoundException이 발생한다.")
        void 주문상태수정_존재하지않는_주문일_경우(){
            // given
            PaymentRequestDTO request = PaymentStep.기본결제요청생성();
            when(orderRepositroy.findById(request.getOrderId())).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> updateOrderStatusUseCase.execute(request))
                    .isInstanceOf(OrderNotFoundException.class);
        }
    }
}
