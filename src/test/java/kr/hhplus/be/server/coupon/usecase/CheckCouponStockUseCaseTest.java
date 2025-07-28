package kr.hhplus.be.server.coupon.usecase;

import kr.hhplus.be.server.coupon.domain.mapper.CouponMapper;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.coupon.exception.CouponNotFoundException;
import kr.hhplus.be.server.coupon.step.CouponStep;
import kr.hhplus.be.server.coupon.usecase.command.UserCouponCommand;
import kr.hhplus.be.server.coupon.usecase.dto.UserCouponRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@DisplayName("쿠폰 수량 체크 테스트")
@ExtendWith(MockitoExtension.class)
public class CheckCouponStockUseCaseTest {

    private CheckCouponStockUseCase checkCouponStockUseCase;

    @Mock
    private CouponRepository couponRepository;

    @BeforeEach
    void setUp() {
        CouponMapper couponMapper = Mappers.getMapper(CouponMapper.class);
        checkCouponStockUseCase = new CheckCouponStockUseCase(
                couponRepository,
                couponMapper
        );
    }

    @Nested
    @DisplayName("쿠폰 수량 체크 성공 케이스")
    class success{

        @Test
        @DisplayName("쿠폰의 수량이 남아있을 경우 예외가 발생되지 않는다.")
        void 쿠폰수량체크() {
            // given
            UserCouponCommand commnad = CouponStep.유저쿠폰커맨드_기본값();
            when(couponRepository.findById(commnad.couponId())).thenReturn(CouponStep.쿠폰엔티티_기본값());

            // when & then
            assertDoesNotThrow(() -> checkCouponStockUseCase.execute(commnad));
        }
    }

    @Nested
    @DisplayName("쿠폰 수량 체크 실패 케이스")
    class fail{

        @Test
        @DisplayName("존재하지 않는 쿠폰일 경우 CouponNotFoundException이 발생한다.")
        void 쿠폰수량체크_존재하지않는_쿠폰일_경우() {
            // given
            UserCouponCommand commnad = CouponStep.유저쿠폰커맨드_기본값();
            when(couponRepository.findById(commnad.couponId())).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> checkCouponStockUseCase.execute(commnad))
                    .isInstanceOf(CouponNotFoundException.class);
        }
    }
}
