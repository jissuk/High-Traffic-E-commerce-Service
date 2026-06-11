package kr.hhplus.be.server.coupon.application.usecase.unit;

import kr.hhplus.be.server.coupon.application.command.UserCouponCommand;
import kr.hhplus.be.server.coupon.application.usecase.RegisterUserCouponUseCase;
import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.coupon.domain.repository.UserCouponRepository;
import kr.hhplus.be.server.coupon.fixture.CouponFixture;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RegisterUserCouponUseCaseUnitTest {

    @InjectMocks
    private RegisterUserCouponUseCase useCase;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private UserCouponRepository userCouponRepository;

    @Nested
    @DisplayName("성공 케이스")
    class success{
        @Test
        void 쿠폰을_발급하면_UserCoupon이_저장된다(){
            // given
//            User user = UserFixture.create();
//            Coupon coupon = CouponFixture.create(); // quantity: 500
            User user = UserFixture.builder().build();
            Coupon coupon = CouponFixture.builder().build();
            UserCouponCommand command = new UserCouponCommand(user.getId(), coupon.getId());
            given(userRepository.findById(user.getId())).willReturn(user);
            given(couponRepository.findById(coupon.getId())).willReturn(coupon);

            // when
            useCase.execute(command);

            // then
            assertThat(coupon.getQuantity()).isEqualTo(499);

            // verify
            verify(userCouponRepository).save(any(UserCoupon.class));
        }
    }
}