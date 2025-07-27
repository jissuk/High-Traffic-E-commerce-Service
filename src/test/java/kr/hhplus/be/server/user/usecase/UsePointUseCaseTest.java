package kr.hhplus.be.server.user.usecase;
import kr.hhplus.be.server.order.domain.mapper.OrderItemMapper;
import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.exception.OrderItemNotFoundException;
import kr.hhplus.be.server.order.step.OrderStep;
import kr.hhplus.be.server.payment.step.PaymentStep;
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
import org.mockito.junit.jupiter.MockitoExtension;

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

    @BeforeEach
    void setUp() {
        UserMapper userMapper = Mappers.getMapper(UserMapper.class);;
        OrderItemMapper orderItemMapper = Mappers.getMapper(OrderItemMapper.class);;
        PointHistoryMapper pointHistoryMapper = Mappers.getMapper(PointHistoryMapper.class);;

        usePointUseCase = new  UsePointUseCase(
                userRepository,
                pointHistoryRepository,
                orderItemRepository,
                userMapper,
                orderItemMapper,
                pointHistoryMapper
        );
    }

    @Nested
    @DisplayName("성공 케이스")
    class success{
        @Test
        void 포인트사용(){
            // given
            long userId = 1L;
            PaymentRequestDTO request = PaymentStep.기본결제요청생성();
            when(userRepository.findById(request.getUserId())).thenReturn(UserStep.기본유저엔티티생성());
            when(orderItemRepository.findById(request.getOrderItemId())).thenReturn(OrderStep.기본주문상세엔티티생성());

            // when
            usePointUseCase.execute(request);

            // then
            verify(userRepository).update(any(UserEntity.class));
            verify(pointHistoryRepository).save(any(PointHistoryEntity.class));
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class fail{
        @Test
        void 포인트사용_존재하지않는_유저일_경우(){
            // given
            long userId = 1L;
            PaymentRequestDTO request = PaymentStep.기본결제요청생성();
            when(userRepository.findById(request.getUserId())).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> usePointUseCase.execute(request))
                    .isInstanceOf(UserNotFoundException.class);
        }

        @Test
        void 포인트사용_존재하지않는_주문상세일_경우(){
            // given
            long userId = 1L;
            PaymentRequestDTO request = PaymentStep.기본결제요청생성();
            when(userRepository.findById(request.getUserId())).thenReturn(UserStep.기본유저엔티티생성());
            when(orderItemRepository.findById(request.getOrderItemId())).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> usePointUseCase.execute(request))
                    .isInstanceOf(OrderItemNotFoundException.class);
        }
    }
}
