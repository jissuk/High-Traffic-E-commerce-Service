package kr.hhplus.be.server.coupon.repository;


import kr.hhplus.be.server.coupon.domain.model.CouponEntity;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.coupon.step.CouponStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.utility.TestcontainersConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Import(TestcontainersConfiguration.class)
@DisplayName("쿠폰 관련 Repository 테스트")
public class CouponRepositoryTest {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        clearTestData();
    }

    private void clearTestData() {
        jdbcTemplate.execute("TRUNCATE TABLE coupons;");
    }

    @Nested
    @DisplayName("성공 케이스")
    class success {

        @Test
        void 쿠폰_등록() {
            // given
            CouponEntity entity = CouponStep.쿠폰엔티티_기본값();

            // when
            CouponEntity saved = couponRepository.save(entity);

            // then
            assertThat(saved.getId()).isNotNull();
        }

        @Test
        void 쿠폰_조회() {
            // given
            CouponEntity saved = couponRepository.save(CouponStep.쿠폰엔티티_기본값());

            // when
            CouponEntity found = couponRepository.findById(saved.getId());

            // then
            assertThat(found).isEqualTo(saved);
        }

    }
}
