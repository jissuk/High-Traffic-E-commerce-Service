package kr.hhplus.be.server.coupon.repository;

import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.CouponEntity;
import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.coupon.domain.model.UserCouponEntity;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.coupon.domain.repository.UserCouponRepository;
import kr.hhplus.be.server.coupon.step.CouponStep;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.step.UserStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.utility.TestcontainersConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DisplayName("유저 쿠폰 관련 Repository 테스트")
public class UserCouponRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        clearTestData();
    }

    private void clearTestData() {
        jdbcTemplate.execute("TRUNCATE TABLE users;");
        jdbcTemplate.execute("TRUNCATE TABLE coupons;");
        jdbcTemplate.execute("TRUNCATE TABLE user_coupons;");
    }

    @Nested
    class success{

//        @Test
//        void 유저쿠폰_등록() {
//            // given
//            UserEntity user = userRepository.save(UserStep.유저엔티티_기본값());
//            CouponEntity coupon = couponRepository.save(CouponStep.쿠폰엔티티_기본값());
//            UserCouponEntity entity = CouponStep.유저쿠폰엔티티_기본값(user, coupon);
//
//            // when
//            UserCouponEntity saved = userCouponRepository.save(entity);
//
//            // then
//            assertThat(saved.getId()).isNotNull();
//        }
//
//        @Test
//        void 유저쿠폰_조회() {
//            // given
//            UserEntity user = userRepository.save(UserStep.유저엔티티_기본값());
//            CouponEntity coupon = couponRepository.save(CouponStep.쿠폰엔티티_기본값());
//            UserCouponEntity saved = userCouponRepository.save(CouponStep.유저쿠폰엔티티_기본값(user, coupon));
//
//            // when
//            UserCouponEntity found = userCouponRepository.findById(saved.getId()).get();
//
//            // then
//            assertThat(found).isNotNull();
//        }

        @Test
        void 유저쿠폰_쿠폰아이디_조회() {
            // given
            User user = userRepository.save(UserStep.유저_기본값());
            Coupon coupon = couponRepository.save(CouponStep.쿠폰_기본값());
            UserCoupon saved = userCouponRepository.save(CouponStep.유저쿠폰_기본값(user.getId(), coupon.getId()));

            // when
            UserCoupon found = userCouponRepository.findByCouponId(coupon.getId()).get();

            // then
            assertThat(found).isEqualTo(saved);
        }

    }
}
