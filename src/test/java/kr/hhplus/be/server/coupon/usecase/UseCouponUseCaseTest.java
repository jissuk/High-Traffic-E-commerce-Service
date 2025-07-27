package kr.hhplus.be.server.coupon.usecase;
import kr.hhplus.be.server.coupon.domain.mapper.UserCouponMapper;
import kr.hhplus.be.server.coupon.domain.model.UserCouponEntity;
import kr.hhplus.be.server.coupon.step.CouponStep;
import kr.hhplus.be.server.order.domain.mapper.OrderItemMapper;
import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.exception.OrderItemNotFoundException;
import kr.hhplus.be.server.order.step.OrderStep;
import kr.hhplus.be.server.payment.step.PaymentStep;
import kr.hhplus.be.server.payment.usecase.dto.PaymentRequestDTO;
import kr.hhplus.be.server.user.domain.repository.UserCouponRepository;
import kr.hhplus.be.server.coupon.domain.service.UserCouponDomainService;
import kr.hhplus.be.server.coupon.exception.UserCouponNotFoundException;
import kr.hhplus.be.server.user.step.UserStep;
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

@DisplayName("쿠폰 사용 테스트")
@ExtendWith(MockitoExtension.class)
public class UseCouponUseCaseTest {

    @InjectMocks
    UseCouponUseCase useCouponUseCase;

    @Mock
    private UserCouponRepository userCouponRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private UserCouponDomainService userCouponDomainService;

    @BeforeEach
    void setUp() {
        OrderItemMapper orderItemMapper = Mappers.getMapper(OrderItemMapper.class);;
        UserCouponMapper userCouponMapper = Mappers.getMapper(UserCouponMapper.class);;

        useCouponUseCase = new UseCouponUseCase(
                userCouponRepository,
                orderItemRepository,
                userCouponDomainService,
                orderItemMapper,
                userCouponMapper
        );
    }

    @Nested
    @DisplayName("성공 케이스")
    class success{

        @Test
        @DisplayName("쿠폰이 존재할 경우 해당 금액만큼 주문 상세의 총 금액이 차감된다.")
        void 쿠폰사용(){
            // given
            PaymentRequestDTO request = PaymentStep.기본결제요청생성();
            when(orderItemRepository.findById(request.getOrderItemId())).thenReturn(OrderStep.기본주문상세엔티티생성());
            when(userCouponRepository.findById(request.getCouponId())).thenReturn(CouponStep.기본유저쿠폰엔티티생성());

            // when
            useCouponUseCase.execute(request);

            // then
            verify(userCouponRepository).save(any(UserCouponEntity.class));
            verify(orderItemRepository).update(any(OrderItemEntity.class));
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class fail{

        @Test
        @DisplayName("존재하지 않는 주문상세일 경우 OrderItemNotFoundException이 발생한다.")
        void 쿠폰사용_존재하지않는_주문상세일_경우(){
            // given
            PaymentRequestDTO request = PaymentStep.기본결제요청생성();
            when(orderItemRepository.findById(request.getOrderItemId())).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> useCouponUseCase.execute(request))
                    .isInstanceOf(OrderItemNotFoundException.class);

        }

        @Test
        @DisplayName("존재하지않는 쿠폰일 경우 UserCouponNotFoundException이 발생한다.")
        void 쿠폰사용_존재하지않는_쿠폰일_경우(){
            // given
            PaymentRequestDTO request = PaymentStep.기본결제요청생성();
            if (request.getCouponId() != null) {
                when(userCouponRepository.findById(request.getCouponId())).thenReturn(null);
            }
            when(orderItemRepository.findById(request.getOrderItemId())).thenReturn(OrderStep.기본주문상세엔티티생성());

            // when & then
            assertThatThrownBy(() -> useCouponUseCase.execute(request))
                    .isInstanceOf(UserCouponNotFoundException.class);

        }
    }
}