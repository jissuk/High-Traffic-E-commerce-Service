//package kr.hhplus.be.server.coupon.usecase;
//
//import kr.hhplus.be.server.coupon.domain.mapper.CouponMapper;
//import kr.hhplus.be.server.coupon.domain.mapper.UserCouponMapper;
//import kr.hhplus.be.server.coupon.domain.model.Coupon;
//import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
//import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
//import kr.hhplus.be.server.coupon.exception.CouponNotFoundException;
//import kr.hhplus.be.server.coupon.step.CouponStep;
//import kr.hhplus.be.server.coupon.usecase.command.UserCouponCommand;
//import kr.hhplus.be.server.coupon.domain.repository.UserCouponRepository;
//import kr.hhplus.be.server.coupon.usecase.reader.CouponReader;
//import kr.hhplus.be.server.user.exception.UserNotFoundException;
//import kr.hhplus.be.server.user.step.UserStep;
//import kr.hhplus.be.server.user.usecase.reader.UserReader;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@DisplayName("유저 쿠폰 등록 테스트")
//@ExtendWith(MockitoExtension.class)
//public class OldRegisterUserCouponUseCaseTest {
//    @InjectMocks
//    private RegisterUserCouponUseCase2 registerUserCouponUseCase;
//    @Mock
//    private CouponReader couponReader;
//    @Mock
//    private UserReader userReader;
//    @Mock
//    private CouponRepository couponRepository;
//    @Mock
//    private UserCouponRepository userCouponRepository;
//
//    @Spy
//    private CouponMapper couponMapper;
//    @Spy
//    private UserCouponMapper userCouponMapper;
//
//    @Nested
//    @DisplayName("유저 쿠폰 등록 성공 케이스")
//    class success{
//
//        @Test
//        @DisplayName("유저와 쿠폰이 존재할 경우 쿠폰을 등록한다.")
//        void 유저쿠폰등록() {
//            // given
//            UserCouponCommand command = CouponStep.유저쿠폰커맨드_기본값();
//            when(userReader.findUserOrThrow(command.userId())).thenReturn(UserStep.유저_기본값());
//            when(couponReader.findCouponOrThrow(command.couponId())).thenReturn(CouponStep.쿠폰_기본값());
//
//
//            // when
//            registerUserCouponUseCase.execute(command);
//
//            // then
//            verify(couponRepository).save(any(Coupon.class));
//            verify(userCouponRepository).save(any(UserCoupon.class));
//
//        }
//    }
//
//    @Nested
//    @DisplayName("유저 쿠폰 등록 실패 케이스")
//    class fail{
//
//        @Test
//        @DisplayName("존재하지 않는 유저일 경우 UserNotFoundException이 발생한다.")
//        void 유저쿠폰등록_존재하지않는_유저일_경우() {
//            // given
//            UserCouponCommand command = CouponStep.유저쿠폰커맨드_기본값();
//            when(userReader.findUserOrThrow(command.userId())).thenThrow(new UserNotFoundException());
//
//
//            // when & then
//            assertThatThrownBy(() -> registerUserCouponUseCase.execute(command))
//                    .isInstanceOf(UserNotFoundException.class);
//        }
//
//        @Test
//        @DisplayName("존재하지 않는 쿠폰일 경우 CouponNotFoundException이 발생한다.")
//        void 유저쿠폰등록_존재하지않는_쿠폰일_경우() {
//            // given
//            UserCouponCommand command = CouponStep.유저쿠폰커맨드_기본값();
//            when(userReader.findUserOrThrow(command.userId())).thenReturn(UserStep.유저_기본값());
//            when(couponReader.findCouponOrThrow(command.couponId())).thenThrow(new CouponNotFoundException());
//
//            // when & then
//            assertThatThrownBy(() -> registerUserCouponUseCase.execute(command))
//                    .isInstanceOf(CouponNotFoundException.class);
//        }
//
//    }
//}
