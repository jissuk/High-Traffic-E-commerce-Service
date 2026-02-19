package kr.hhplus.be.server.user.usecase.unit;

import kr.hhplus.be.server.user.domain.mapper.UserResponseMapper;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.usecase.CreateUserUseCase;
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
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Testcontainers
@DisplayName("유저 생성 테스트")
@ExtendWith(MockitoExtension.class)
public class CreateUserUseCaseTest {
    @InjectMocks
    private CreateUserUseCase createUserUseCase;
    @Mock
    private UserRepository userRepository;
    @Spy
    private UserResponseMapper userResponseMapper;

    @Nested
    @DisplayName("유저 생성 성공 케이스")
    class success{

        @Test
        @DisplayName("유저를 생성하면 유저와 포인트 내역이 등록이 된다.")
        void 유저생성(){
            // given
            UserCommand command = new UserCommand(null, 3000L);
            User user = User.builder()
                            .id(1L)
                            .point(3000L)
                            .build();
            when(userRepository.save(any(User.class))).thenReturn(user);

            // when
            UserResponse result = createUserUseCase.execute(command);

            // then
            assertAll(
                ()-> assertThat(result.userId())
                        .as("유저 생성 확인")
                        .isEqualTo(1L),
                ()-> assertThat(result.point())
                        .as("충전된 포인트 확인")
                        .isEqualTo(3000L)
            );
        }
    }
}
