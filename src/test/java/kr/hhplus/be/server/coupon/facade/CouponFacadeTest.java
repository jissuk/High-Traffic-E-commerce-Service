package kr.hhplus.be.server.coupon.facade;

import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.coupon.step.CouponStep;
import kr.hhplus.be.server.coupon.usecase.dto.UserCouponRequestDTO;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.step.UserStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@DisplayName("선착순 쿠폰 발급 테스트")
public class CouponFacadeTest {

    @Autowired
    private CouponFacade couponFacade;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        clearRepositories();
        initTestData();
    }

    private void clearRepositories() {
        couponRepository.clear();
        userRepository.clear();
    }

    private void initTestData() {
        couponRepository.save(CouponStep.기본쿠폰엔티티생성());
        userRepository.save(UserStep.기본유저엔티티생성());
    }

    @Nested
    @DisplayName("성공 케이스")
    class success{
        @Test
        void 선착순쿠폰발급() {
            // given
            UserCouponRequestDTO request = CouponStep.기본유저쿠폰요청생성();

            // when & then
            assertDoesNotThrow(() -> couponFacade.issueCoupon(request));
        }
    }
}
