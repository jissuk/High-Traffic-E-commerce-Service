package kr.hhplus.be.server.coupon.usecase;

import kr.hhplus.be.server.coupon.domain.mapper.CouponMapper;
import kr.hhplus.be.server.coupon.domain.mapper.UserCouponMapper;
import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.CouponEntity;
import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.coupon.domain.model.UserCouponEntity;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.coupon.exception.CouponNotFoundException;
import kr.hhplus.be.server.coupon.step.CouponStep;
import kr.hhplus.be.server.coupon.usecase.dto.UserCouponRequestDTO;
import kr.hhplus.be.server.user.domain.model.PointHistoryEntity;
import kr.hhplus.be.server.user.domain.model.UserEntity;
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
    @DisplayName("성공 케이스")
    class success{

        @Test
        void 유저쿠폰등록() {
            // given
            UserCouponRequestDTO request = CouponStep.기본유저쿠폰요청생성();
            when(couponRepository.findById(request.getCouponId())).thenReturn(CouponStep.기본쿠폰엔티티생성());
            when(userRepository.findById(request.getCouponId())).thenReturn(UserStep.기본유저엔티티생성());

            // when
            registerUserCouponUseCase.execute(request);

            // then
            verify(couponRepository).update(any(CouponEntity.class));
            verify(userCouponRepository).save(any(UserCouponEntity.class));

        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class fail{
        @Test
        void 유저쿠폰등록_존재하지않는_쿠폰일_경우() {
            // given
            UserCouponRequestDTO request = CouponStep.기본유저쿠폰요청생성();
            when(couponRepository.findById(request.getCouponId())).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> registerUserCouponUseCase.execute(request))
                    .isInstanceOf(CouponNotFoundException.class);
        }

        @Test
        void 유저쿠폰등록_존재하지않는_유저일_경우() {
            // given
            UserCouponRequestDTO request = CouponStep.기본유저쿠폰요청생성();
            when(couponRepository.findById(request.getCouponId())).thenReturn(CouponStep.기본쿠폰엔티티생성());
            when(userRepository.findById(request.getCouponId())).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> registerUserCouponUseCase.execute(request))
                    .isInstanceOf(UserNotFoundException.class);
        }
    }
}
