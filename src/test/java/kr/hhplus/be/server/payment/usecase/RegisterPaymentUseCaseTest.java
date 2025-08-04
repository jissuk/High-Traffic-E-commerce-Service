package kr.hhplus.be.server.payment.usecase;

import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.exception.OrderItemNotFoundException;
import kr.hhplus.be.server.order.step.OrderStep;
import kr.hhplus.be.server.order.usecase.command.OrderItemCommand;
import kr.hhplus.be.server.order.usecase.reader.OrderItemReader;
import kr.hhplus.be.server.payment.domain.Repository.PaymentRepository;
import kr.hhplus.be.server.payment.domain.mapper.PaymentMapper;
import kr.hhplus.be.server.payment.domain.model.PaymentEntity;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.exception.UserNotFoundException;
import kr.hhplus.be.server.user.step.UserStep;
import kr.hhplus.be.server.user.usecase.reader.UserReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("결제 등록 테스트")
@ExtendWith(MockitoExtension.class)
public class RegisterPaymentUseCaseTest {
    @InjectMocks
    RegisterPaymentUseCase registerPaymentUseCase;
    
    @Mock
    private UserReader userReader;
    @Mock
    private OrderItemReader orderItemReader;
    @Mock
    private PaymentRepository paymentRepository;

    @Spy
    private PaymentMapper paymentMapper;

    @Nested
    @DisplayName("결제 등록 성공 케이스")
    class success {

        @Test
        @DisplayName("유저와 주문상세가 존재할 경우 결제를 등록한다.")
        void 결제등록(){
            // given
            long userId = 1L;
            OrderItemCommand command = OrderStep.주문상세커맨드_기본값();
            when(userReader.findUserOrThrow(userId)).thenReturn(UserStep.유저_기본값());
            when(orderItemReader.findOrderItemOrThrow(command.orderItemId())).thenReturn(OrderStep.주문상세_기본값());

            // when
            registerPaymentUseCase.execute(command);

            // then
            verify(paymentRepository).save(any(PaymentEntity.class));
        }
    }

    @Nested
    @DisplayName("결제 등록 실패 케이스")
    class fail{

        @Test
        @DisplayName("존재하지 유저일 경우 UserNotFoundException이 발생한다.")
        void 결제등록_존재하지않는_유저일_경우(){
            // given
            long userId = 1L;
            OrderItemCommand command = OrderStep.주문상세커맨드_기본값();
            when(userReader.findUserOrThrow(userId)).thenThrow(new UserNotFoundException());

            // when & then
            assertThatThrownBy(() -> registerPaymentUseCase.execute(command))
                    .isInstanceOf(UserNotFoundException.class);
        }

        @Test
        @DisplayName("존재하지 않는 주문상세일 경우 OrderItemNotFoundException이 발생한다.")
        void 결제등록_존재하지않는_주문상세일_경우(){
            // given
            long userId = 1L;
            OrderItemCommand command = OrderStep.주문상세커맨드_기본값();
            when(userReader.findUserOrThrow(userId)).thenReturn(UserStep.유저_기본값());
            when(orderItemReader.findOrderItemOrThrow(command.orderItemId())).thenThrow(new OrderItemNotFoundException());

            // when & then
            assertThatThrownBy(() -> registerPaymentUseCase.execute(command))
                    .isInstanceOf(OrderItemNotFoundException.class);
        }
    }
}
