package kr.hhplus.be.server.user.usecase;
import kr.hhplus.be.server.user.domain.mapper.UserResponseMapper;
import kr.hhplus.be.server.user.exception.UserNotFoundException;
import kr.hhplus.be.server.user.step.UserStep;
import kr.hhplus.be.server.user.usecase.reader.UserReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@DisplayName("유저 조회 테스트")
@ExtendWith(MockitoExtension.class)
public class GetUserUseCaseTest {

    @InjectMocks
    private GetUserUseCase getUserUseCase;

    @Mock
    private UserReader userReader;

    @Spy
    private UserResponseMapper userResponseMapper;

    @Nested
    @DisplayName("유저 조회 성공 케이스")
    class success{

        @Test
        @DisplayName("유저가 존재하면 정상적으로 유저를 조회한다.")
        void 유저조회(){
            // given
            long userId = 1L;
            when(userReader.findUserOrThrow(userId)).thenReturn(UserStep.유저_기본값());

            // when & then
            assertDoesNotThrow(() -> getUserUseCase.execute(userId));
        }
    }

    @Nested
    @DisplayName("유저 조회 실패 케이스")
    class fail{

        @Test
        @DisplayName("존재하지 않는 유저일 경우 UserNotFoundException이 발생한다.")
        void 유저조회_존재하지않는_유저일_경우(){
            // given
            long userId = 1L;
            when(userReader.findUserOrThrow(userId)).thenThrow(new UserNotFoundException());

            // when & then
            assertThatThrownBy(() -> getUserUseCase.execute(userId))
                    .isInstanceOf(UserNotFoundException.class);
        }
    }
}
