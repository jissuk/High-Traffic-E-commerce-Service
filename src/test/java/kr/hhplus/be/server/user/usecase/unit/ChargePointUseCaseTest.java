package kr.hhplus.be.server.user.usecase.unit;
import kr.hhplus.be.server.user.domain.mapper.UserResponseMapper;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.exception.UserNotFoundException;
import kr.hhplus.be.server.user.step.UserStep;
import kr.hhplus.be.server.user.usecase.ChargePointUseCase;
import kr.hhplus.be.server.user.usecase.command.UserCommand;
import kr.hhplus.be.server.user.usecase.dto.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.support.TransactionTemplate;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;


@DisplayName("포인트 충전 테스트")
@ExtendWith(MockitoExtension.class)
public class ChargePointUseCaseTest {

    @InjectMocks
    private ChargePointUseCase chargePointUseCase;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TransactionTemplate transactionTemplate;

    @Spy
    private ApplicationEventPublisher applicationEventPublisher;
    @Spy
    private UserResponseMapper userResponseMapper;


    @Nested
    @DisplayName("포인트 충전 성공 케이스")
    class success{

        @Test
        @DisplayName("유저가 존재할 경우 유저의 포인트를 충전한다.")
        void 포인트충전(){
            // given
            UserCommand command = UserStep.defaultUserCommand(); // point: 3000L
            when(userRepository.findById(command.userId())).thenReturn(new User(1L, 10_000L));

            // when
            UserResponse result = chargePointUseCase.execute(command);

            // then
            assertThat(result.point()).isEqualTo(13000L);
        }
    }

    @Nested
    @DisplayName("포인트 충전 실패 케이스")
    class fail{

        @Test
        @DisplayName("존재하지 않는 유저일 경우 UserNotFoundException이 발생한다.")
        void 포인트충전_존재하지않는_유저일_경우(){
            // given
            UserCommand command = UserStep.defaultUserCommand();
            when(userRepository.findById(command.userId())).thenThrow(new UserNotFoundException());

            // when & then
            assertThatThrownBy(() -> chargePointUseCase.execute(command))
                    .isInstanceOf(UserNotFoundException.class);
        }
    }

}
