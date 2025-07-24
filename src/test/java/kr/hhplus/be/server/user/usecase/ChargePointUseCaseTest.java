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
import kr.hhplus.be.server.user.usecase.dto.UserRequestDTO;
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

@DisplayName("포인트 충전 테스트")
@ExtendWith(MockitoExtension.class)
public class ChargePointUseCaseTest {

    @InjectMocks
    private ChargePointUseCase chargePointUseCase;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @BeforeEach
    void setUp() {
        UserMapper userMapper = Mappers.getMapper(UserMapper.class);;
        PointHistoryMapper pointHistoryMapper = Mappers.getMapper(PointHistoryMapper.class);;
        UserResponseMapper userResponseMapper = Mappers.getMapper(UserResponseMapper.class);;

        chargePointUseCase = new ChargePointUseCase(
                userRepository,
                pointHistoryRepository,
                userMapper,
                pointHistoryMapper,
                userResponseMapper
        );
    }

    @Nested
    @DisplayName("성공 케이스")
    class success{
        @Test
        void 포인트충전(){
            // given
            long userId = 1L;
            UserRequestDTO request = UserStep.기본유저요청생성();
            when(userRepository.findById(userId)).thenReturn(UserStep.기본유저엔티티생성());
    
            // when
            chargePointUseCase.execute(request);
    
            // then
            verify(userRepository).update(any(UserEntity.class));
            verify(pointHistoryRepository).save(any(PointHistoryEntity.class));
        }
    }
    
    @Nested
    @DisplayName("실패 케이스")
    class fail{
        @Test
        void 포인트충전_존재하지않는_유저일_경우(){
            // given
            long userId = 1L;
            UserRequestDTO request = UserStep.기본유저요청생성();
            when(userRepository.findById(userId)).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> chargePointUseCase.execute(request))
                    .isInstanceOf(UserNotFoundException.class);
        }
    }

}
