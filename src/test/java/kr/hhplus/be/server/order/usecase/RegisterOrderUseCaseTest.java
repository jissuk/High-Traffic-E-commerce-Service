package kr.hhplus.be.server.order.usecase;

import kr.hhplus.be.server.order.domain.mapper.OrderMapper;
import kr.hhplus.be.server.order.domain.model.OrderEntity;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@DisplayName("주문 등록 테스트")
@ExtendWith(MockitoExtension.class)
public class RegisterOrderUseCaseTest {

    private RegisterOrderUseCase registerOrderUseCase;

    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepositroy orderRepositroy;

    @BeforeEach
    void setUp() {
        UserMapper userMapper = Mappers.getMapper(UserMapper.class);
        OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

        registerOrderUseCase = new RegisterOrderUseCase(
                userRepository,
                orderRepositroy,
                userMapper,
                orderMapper
        );
    }

    @Nested
    @DisplayName("주문 등록 성공 케이스")
    class success{

        @Test
        @DisplayName("유저가 존재할 경우 주문을 등록한다.")
        void 주문등록(){
            // given
            long userId = 1L;
            when(userRepository.findById(userId)).thenReturn(UserStep.유저엔티티_기본값());

            // when
            registerOrderUseCase.execute(userId);

            // then
            verify(orderRepositroy).save(any(OrderEntity.class));
        }
    }
    @Nested
    @DisplayName("주문 등록 실패 케이스")
    class fail{

        @Test
        @DisplayName("존재하지 않은 유저일 경우 UserNotFoundException이 발생한다.")
        void 주문등록_존재하지않는_유저일_경우(){

            // given
            long userId = 1L;
            when(userRepository.findById(userId)).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> registerOrderUseCase.execute(userId))
                    .isInstanceOf(UserNotFoundException.class);
        }
    }
}
