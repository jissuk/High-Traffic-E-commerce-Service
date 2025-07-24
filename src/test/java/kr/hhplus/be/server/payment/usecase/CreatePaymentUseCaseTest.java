package kr.hhplus.be.server.payment.usecase;

import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.exception.OrderItemNotFoundException;
import kr.hhplus.be.server.order.step.OrderStep;
import kr.hhplus.be.server.order.usecase.dto.OrderItemRequestDTO;
import kr.hhplus.be.server.payment.domain.Repository.PaymentHistoryRepository;
import kr.hhplus.be.server.payment.domain.Repository.PaymentRepository;
import kr.hhplus.be.server.payment.domain.mapper.PaymentHistoryMapper;
import kr.hhplus.be.server.payment.domain.mapper.PaymentMapper;
import kr.hhplus.be.server.payment.domain.model.PaymentEntity;
import kr.hhplus.be.server.payment.domain.model.PaymentHistory;
import kr.hhplus.be.server.payment.domain.model.PaymentHistoryEntity;
import kr.hhplus.be.server.user.domain.mapper.UserMapper;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.exception.UserNotFoundException;
import kr.hhplus.be.server.user.step.UserStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("결제 생성 테스트")
@ExtendWith(MockitoExtension.class)
public class CreatePaymentUseCaseTest {

    CreatePaymentUseCase createPaymentUseCase;

    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private PaymentHistoryRepository paymentHistoryRepository;

    @BeforeEach
    void setUp() {
        PaymentMapper paymentMapper = Mappers.getMapper(PaymentMapper.class);
        PaymentHistoryMapper paymentHistoryMapper = Mappers.getMapper(PaymentHistoryMapper.class);
        createPaymentUseCase = new CreatePaymentUseCase(
                userRepository,
                orderItemRepository,
                paymentRepository,
                paymentHistoryRepository,
                paymentMapper,
                paymentHistoryMapper
        );
    }

    @Nested
    @DisplayName("성공 케이스")
    class success {
        @Test
        void 결제생성(){
            // given
            long userId = 1L;
            OrderItemRequestDTO request = OrderStep.기본주문상세요청생성();
            when(userRepository.findById(userId)).thenReturn(UserStep.기본유저엔티티생성());
            when(orderItemRepository.findById(request.getOrderItemId())).thenReturn(OrderStep.기본주문상세엔티티생성());

            // when
            createPaymentUseCase.execute(request);

            // then
            verify(paymentRepository).save(any(PaymentEntity.class));
            verify(paymentHistoryRepository).save(any(PaymentHistoryEntity.class));
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class fail{
        @Test
        void 결제생성_존재하지않는_유저일_경우(){
            // given
            long userId = 1L;
            OrderItemRequestDTO request = OrderStep.기본주문상세요청생성();
            when(userRepository.findById(userId)).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> createPaymentUseCase.execute(request))
                    .isInstanceOf(UserNotFoundException.class);
        }

        @Test
        void 결제생성_존재하지않는_주문상세일_경우(){
            // given
            long userId = 1L;
            OrderItemRequestDTO request = OrderStep.기본주문상세요청생성();
            when(userRepository.findById(userId)).thenReturn(UserStep.기본유저엔티티생성());
            when(orderItemRepository.findById(request.getOrderItemId())).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> createPaymentUseCase.execute(request))
                    .isInstanceOf(OrderItemNotFoundException.class);
        }
    }
}
