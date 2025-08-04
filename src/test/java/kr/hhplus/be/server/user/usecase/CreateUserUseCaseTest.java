package kr.hhplus.be.server.user.usecase;
import kr.hhplus.be.server.user.domain.mapper.PointHistoryMapper;
import kr.hhplus.be.server.user.domain.mapper.UserMapper;
import kr.hhplus.be.server.user.domain.model.PointHistoryEntity;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import kr.hhplus.be.server.user.domain.repository.PointHistoryRepository;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.step.UserStep;
import kr.hhplus.be.server.user.usecase.command.UserCommand;
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
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@Testcontainers
@DisplayName("유저 생성 테스트")
@ExtendWith(MockitoExtension.class)
public class CreateUserUseCaseTest {

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @Spy
    private UserMapper userMapper;
    @Spy
    private PointHistoryMapper pointHistoryMapper;

    @Nested
    @DisplayName("유저 생성 성공 케이스")
    class success{

        @Test
        @DisplayName("유저를 생성하면 유저와 포인트 내역이 등록이 된다.")
        void 유저생성(){
            // given
            UserCommand command = UserStep.유저커맨드_기본값();

            // when
            createUserUseCase.execute(command);

            // then
            verify(userRepository).save(any(UserEntity.class));
            verify(pointHistoryRepository).save(any(PointHistoryEntity.class));
        }
    }
}
