//package kr.hhplus.be.server.coupon.usecase;
//import kr.hhplus.be.server.coupon.domain.mapper.UserCouponMapper;
//import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
//import kr.hhplus.be.server.coupon.step.CouponStep;
//import kr.hhplus.be.server.coupon.usecase.reader.UserCouponReader;
//import kr.hhplus.be.server.order.domain.mapper.OrderItemMapper;
//import kr.hhplus.be.server.order.domain.model.OrderItem;
//import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
//import kr.hhplus.be.server.order.exception.OrderItemNotFoundException;
//import kr.hhplus.be.server.order.step.OrderStep;
//import kr.hhplus.be.server.order.usecase.reader.OrderItemReader;
//import kr.hhplus.be.server.payment.step.PaymentStep;
//import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
//import kr.hhplus.be.server.coupon.domain.repository.UserCouponRepository;
//import kr.hhplus.be.server.coupon.domain.service.UserCouponDomainService;
//import kr.hhplus.be.server.coupon.exception.UserCouponNotFoundException;
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
//@DisplayName("쿠폰 사용 테스트")
//@ExtendWith(MockitoExtension.class)
//public class OldUseCouponUseCaseTest {
//
//    @InjectMocks
//    UseCouponUseCase useCouponUseCase;
//
//    @Mock
//    private OrderItemReader orderItemReader;
//    @Mock
//    private UserCouponReader userCouponReader;
//
//    @Mock
//    private UserCouponRepository userCouponRepository;
//    @Mock
//    private OrderItemRepository orderItemRepository;
//
//    @Mock
//    private UserCouponDomainService userCouponDomainService;
//    @Spy
//    OrderItemMapper orderItemMapper;
//    @Spy
//    UserCouponMapper userCouponMapper;
//
//    @Nested
//    @DisplayName("쿠폰 사용 성공 케이스")
//    class success{
//
//        @Test
//        @DisplayName("쿠폰이 존재할 경우 해당 금액만큼 주문 상세의 총 금액이 차감된다.")
//        void 쿠폰사용() throws InterruptedException {
//            // given
//            PaymentCommand command = PaymentStep.결제커맨드_기본값();
//            when(orderItemReader.findOrderItemOrThrow(command.orderItemId())).thenReturn(OrderStep.주문상세_기본값());
//            when(userCouponReader.findByCouponIdUserCouponOrThrow(command.couponId())).thenReturn(CouponStep.유저쿠폰_기본값());
//
//            // when
//            useCouponUseCase.execute(command);
//
//            // then
//            verify(userCouponRepository).save(any(UserCoupon.class));
//            verify(orderItemRepository).save(any(OrderItem.class));
//        }
//    }
//
//    @Nested
//    @DisplayName("쿠폰 사용 실패 케이스")
//    class fail{
//
//        @Test
//        @DisplayName("존재하지 않는 주문상세일 경우 OrderItemNotFoundException이 발생한다.")
//        void 쿠폰사용_존재하지않는_주문상세일_경우(){
//            // given
//            PaymentCommand command = PaymentStep.결제커맨드_기본값();
//            when(orderItemReader.findOrderItemOrThrow(command.orderItemId())).thenThrow(new OrderItemNotFoundException());
//
//            // when & then
//            assertThatThrownBy(() -> useCouponUseCase.execute(command))
//                    .isInstanceOf(OrderItemNotFoundException.class);
//
//        }
//
//        @Test
//        @DisplayName("존재하지않는 쿠폰일 경우 UserCouponNotFoundException이 발생한다.")
//        void 쿠폰사용_존재하지않는_쿠폰일_경우(){
//            // given
//            PaymentCommand command = PaymentStep.결제커맨드_기본값();
//            when(orderItemReader.findOrderItemOrThrow(command.orderItemId())).thenReturn(OrderStep.주문상세_기본값());
//            when(userCouponReader.findByCouponIdUserCouponOrThrow(command.couponId())).thenThrow(new UserCouponNotFoundException());
//
//            // when & then
//            assertThatThrownBy(() -> useCouponUseCase.execute(command))
//                    .isInstanceOf(UserCouponNotFoundException.class);
//        }
//    }
//}