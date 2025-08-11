//package kr.hhplus.be.server.coupon.usecase;
//
//import kr.hhplus.be.server.common.annotation.UseCase;
//import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
//import kr.hhplus.be.server.coupon.usecase.reader.UserCouponReader;
//import kr.hhplus.be.server.order.domain.model.OrderItem;
//import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
//import kr.hhplus.be.server.order.usecase.reader.OrderItemReader;
//import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
//import kr.hhplus.be.server.coupon.domain.repository.UserCouponRepository;
//import kr.hhplus.be.server.coupon.domain.service.UserCouponDomainService;
//import lombok.RequiredArgsConstructor;
//
//
//@UseCase
//@RequiredArgsConstructor
//public class UseCouponUseCase {
//
//    private final UserCouponReader userCouponReader;
//    private final OrderItemReader orderItemReader;
//
//    private final UserCouponRepository userCouponRepository;
//    private final OrderItemRepository orderItemRepository;
//    private final UserCouponDomainService userCouponDomainService;
//
//    public void execute(PaymentCommand command) {
//
//        OrderItem orderItem = orderItemReader.findOrderItemOrThrow(command.orderItemId());
//        UserCoupon userCoupon = userCouponReader.findByCouponIdUserCouponOrThrow(command.couponId());
//
//        userCouponDomainService.applyCoupon(orderItem, userCoupon);
//
//        userCouponRepository.save(userCoupon);
//        orderItemRepository.save(orderItem);
//    }
//}
