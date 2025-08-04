package kr.hhplus.be.server.user.usecase;
import kr.hhplus.be.server.user.domain.mapper.PointHistoryMapper;
import kr.hhplus.be.server.user.domain.mapper.UserMapper;
import kr.hhplus.be.server.user.domain.mapper.UserResponseMapper;
import kr.hhplus.be.server.user.domain.model.PointHistoryEntity;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import kr.hhplus.be.server.user.domain.repository.PointHistoryRepository;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.exception.UserNotFoundException;
import kr.hhplus.be.server.user.step.UserStep;
import kr.hhplus.be.server.user.usecase.command.UserCommand;
import kr.hhplus.be.server.user.usecase.dto.UserResponseDTO;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@DisplayName("포인트 충전 테스트")
@ExtendWith(MockitoExtension.class)
public class ChargePointUseCaseTest {

    @InjectMocks
    private ChargePointUseCase chargePointUseCase;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PointHistoryRepository pointHistoryRepository;
    @Spy
    private UserMapper userMapper;
    @Spy
    private PointHistoryMapper pointHistoryMapper;
    @Spy
    private UserResponseMapper userResponseMapper;

    @Nested
    @DisplayName("포인트 충전 성공 케이스")
    class success{

        @Test
        @DisplayName("유저가 존재할 경우 유저의 포인트를 충전한다.")
        void 포인트충전(){
            // given
            long userId = 1L;
            UserCommand command = UserStep.유저커맨드_기본값();
            UserEntity user = UserStep.유저엔티티_기본값();
            when(userRepository.findById(userId)).thenReturn(user);

            // when
            UserResponseDTO result = chargePointUseCase.execute(command);

            // then
            verify(userRepository).save(any(UserEntity.class));
            verify(pointHistoryRepository).save(any(PointHistoryEntity.class));
            assertThat(result.getPoint()).isEqualTo(command.point() + user.getPoint());
        }
    }

    @Nested
    @DisplayName("포인트 충전 실패 케이스")
    class fail{

        @Test
        @DisplayName("존재하지 않는 유저일 경우 UserNotFoundException이 발생한다.")
        void 포인트충전_존재하지않는_유저일_경우(){
            // given
            long userId = 1L;
            UserCommand command = UserStep.유저커맨드_기본값();
            when(userRepository.findById(userId)).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> chargePointUseCase.execute(command))
                    .isInstanceOf(UserNotFoundException.class);
        }
    }

}
