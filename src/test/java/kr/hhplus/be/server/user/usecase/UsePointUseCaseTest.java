package kr.hhplus.be.server.user.usecase;
import kr.hhplus.be.server.order.domain.mapper.OrderItemMapper;
import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.exception.OrderItemNotFoundException;
import kr.hhplus.be.server.order.step.OrderStep;
import kr.hhplus.be.server.payment.step.PaymentStep;
import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
import kr.hhplus.be.server.payment.usecase.dto.PaymentRequestDTO;
import kr.hhplus.be.server.user.domain.mapper.PointHistoryMapper;
import kr.hhplus.be.server.user.domain.mapper.UserMapper;
import kr.hhplus.be.server.user.domain.model.PointHistoryEntity;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import kr.hhplus.be.server.user.domain.repository.PointHistoryRepository;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("포인트 사용 테스트")
@ExtendWith(MockitoExtension.class)
public class UsePointUseCaseTest {

    @InjectMocks
    private UsePointUseCase usePointUseCase;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PointHistoryRepository pointHistoryRepository;
    @Mock
    private OrderItemRepository orderItemRepository;

    @Spy
    private UserMapper userMapper;
    @Spy
    private OrderItemMapper orderItemMapper;
    @Spy
    private PointHistoryMapper pointHistoryMapper;

    @Nested
    @DisplayName("포인트 사용 성공 케이스")
    class success{

        @Test
        @DisplayName("유저와 주문상세가 존재할 경우 포인트를 사용한다.")
        void 포인트사용(){
            // given
            PaymentCommand command = PaymentStep.결제커맨드_기본값();
            when(userRepository.findById(command.userId())).thenReturn(UserStep.유저엔티티_기본값());
            when(orderItemRepository.findById(command.orderItemId())).thenReturn(OrderStep.주문상세엔티티_기본값());

            // when
            usePointUseCase.execute(command);

            // then
            verify(userRepository).save(any(UserEntity.class));
            verify(pointHistoryRepository).save(any(PointHistoryEntity.class));
        }
    }

    @Nested
    @DisplayName("포인트 사용실패 케이스")
    class fail{

        @Test
        @DisplayName("존재하지 않는 유저일 경우 UserNotFoundException이 발생한다.")
        void 포인트사용_존재하지않는_유저일_경우(){
            // given
            PaymentCommand command = PaymentStep.결제커맨드_기본값();
            when(userRepository.findById(command.userId())).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> usePointUseCase.execute(command))
                    .isInstanceOf(UserNotFoundException.class);
        }

        @Test
        @DisplayName("존재하지 않는 주문상세일 경우 OrderItemNotFoundException이 발생한다.")
        void 포인트사용_존재하지않는_주문상세일_경우(){
            // given
            PaymentCommand command = PaymentStep.결제커맨드_기본값();
            when(userRepository.findById(command.userId())).thenReturn(UserStep.유저엔티티_기본값());
            when(orderItemRepository.findById(command.orderItemId())).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> usePointUseCase.execute(command))
                    .isInstanceOf(OrderItemNotFoundException.class);
        }
    }
}
