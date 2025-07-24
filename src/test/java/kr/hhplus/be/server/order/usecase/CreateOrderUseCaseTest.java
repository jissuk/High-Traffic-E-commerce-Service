package kr.hhplus.be.server.order.usecase;

import kr.hhplus.be.server.order.domain.mapper.OrderHistoryMapper;
import kr.hhplus.be.server.order.domain.mapper.OrderItemMapper;
import kr.hhplus.be.server.order.domain.mapper.OrderMapper;
import kr.hhplus.be.server.order.domain.model.OrderEntity;
import kr.hhplus.be.server.order.domain.model.OrderHistoryEntity;
import kr.hhplus.be.server.order.domain.repository.OrderHistoryRepository;
import kr.hhplus.be.server.order.domain.repository.OrderRepositroy;
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


@DisplayName("주문 생성 테스트")
@ExtendWith(MockitoExtension.class)
public class CreateOrderUseCaseTest {

    private CreateOrderUseCase createOrderUseCase;

    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepositroy orderRepositroy;
    @Mock
    private OrderHistoryRepository orderHistoryRepository;

    @BeforeEach
    void setUp() {
        UserMapper userMapper = Mappers.getMapper(UserMapper.class);
        OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);
        OrderHistoryMapper orderHistoryMapper = Mappers.getMapper(OrderHistoryMapper.class);

        createOrderUseCase = new CreateOrderUseCase(
                userRepository,
                orderRepositroy,
                orderHistoryRepository,
                userMapper,
                orderMapper,
                orderHistoryMapper
        );
    }

    @Nested
    @DisplayName("성공 케이스")
    class success{
        @Test
        void 주문생성(){
            // given
            long userId = 1L;
            when(userRepository.findById(userId)).thenReturn(UserStep.기본유저엔티티생성());

            // when
            createOrderUseCase.execute(userId);

            // then
            verify(orderRepositroy).save(any(OrderEntity.class));
            verify(orderHistoryRepository).save(any(OrderHistoryEntity.class));
        }
    }
    @Nested
    @DisplayName("실패 케이스")
    class fail{
        @Test
        void 주문생성_존재하지않는_유저일_경우(){

            // given
            long userId = 1L;
            when(userRepository.findById(userId)).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> createOrderUseCase.execute(userId))
                    .isInstanceOf(UserNotFoundException.class);
        }
    }
}
