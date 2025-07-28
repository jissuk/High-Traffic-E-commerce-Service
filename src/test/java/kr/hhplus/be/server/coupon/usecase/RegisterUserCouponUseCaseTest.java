package kr.hhplus.be.server.coupon.usecase;

import kr.hhplus.be.server.coupon.domain.mapper.CouponMapper;
import kr.hhplus.be.server.coupon.domain.mapper.UserCouponMapper;
import kr.hhplus.be.server.coupon.domain.model.CouponEntity;
import kr.hhplus.be.server.coupon.domain.model.UserCouponEntity;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.coupon.exception.CouponNotFoundException;
import kr.hhplus.be.server.coupon.step.CouponStep;
import kr.hhplus.be.server.coupon.usecase.command.UserCouponCommand;
import kr.hhplus.be.server.coupon.usecase.dto.UserCouponRequestDTO;
import kr.hhplus.be.server.user.domain.repository.UserCouponRepository;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("유저 쿠폰 등록 테스트")
@ExtendWith(MockitoExtension.class)
public class RegisterUserCouponUseCaseTest {

    private RegisterUserCouponUseCase registerUserCouponUseCase;

    @Mock
    private CouponRepository couponRepository;
    @Mock
    private UserCouponRepository userCouponRepository;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        CouponMapper couponMapper = Mappers.getMapper(CouponMapper.class);
        UserCouponMapper userCouponMapper = Mappers.getMapper(UserCouponMapper.class);

        registerUserCouponUseCase = new RegisterUserCouponUseCase(
                couponRepository,
                userCouponRepository,
                userRepository,
                couponMapper,
                userCouponMapper
        );
    }

    @Nested
    @DisplayName("유저 쿠폰 등록 성공 케이스")
    class success{

        @Test
        @DisplayName("유저와 쿠폰이 존재할 경우 쿠폰을 등록한다.")
        void 유저쿠폰등록() {
            // given
            UserCouponCommand command = CouponStep.유저쿠폰커맨드_기본값();
            when(couponRepository.findById(command.couponId())).thenReturn(CouponStep.쿠폰엔티티_기본값());
            when(userRepository.findById(command.userId())).thenReturn(UserStep.유저엔티티_기본값());

            // when
            registerUserCouponUseCase.execute(command);

            // then
            verify(couponRepository).update(any(CouponEntity.class));
            verify(userCouponRepository).save(any(UserCouponEntity.class));

        }
    }

    @Nested
    @DisplayName("유저 쿠폰 등록 실패 케이스")
    class fail{

        @Test
        @DisplayName("존재하지 않는 쿠폰일 경우 CouponNotFoundException이 발생한다.")
        void 유저쿠폰등록_존재하지않는_쿠폰일_경우() {
            // given
            UserCouponCommand command = CouponStep.유저쿠폰커맨드_기본값();
            when(couponRepository.findById(command.couponId())).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> registerUserCouponUseCase.execute(command))
                    .isInstanceOf(CouponNotFoundException.class);
        }

        @Test
        @DisplayName("존재하지 않는 유저일 경우 UserNotFoundException이 발생한다.")
        void 유저쿠폰등록_존재하지않는_유저일_경우() {
            // given
            UserCouponCommand command = CouponStep.유저쿠폰커맨드_기본값();
            when(couponRepository.findById(command.couponId())).thenReturn(CouponStep.쿠폰엔티티_기본값());
            when(userRepository.findById(command.userId())).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> registerUserCouponUseCase.execute(command))
                    .isInstanceOf(UserNotFoundException.class);
        }
    }
}
