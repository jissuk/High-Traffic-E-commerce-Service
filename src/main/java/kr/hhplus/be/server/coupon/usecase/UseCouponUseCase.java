package kr.hhplus.be.server.coupon.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.coupon.domain.mapper.UserCouponMapper;
import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.coupon.domain.model.UserCouponEntity;
import kr.hhplus.be.server.coupon.usecase.reader.UserCouponReader;
import kr.hhplus.be.server.order.domain.mapper.OrderItemMapper;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.exception.OrderItemNotFoundException;
import kr.hhplus.be.server.order.usecase.reader.OrderItemReader;
import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
import kr.hhplus.be.server.coupon.domain.repository.UserCouponRepository;
import kr.hhplus.be.server.coupon.domain.service.UserCouponDomainService;
import kr.hhplus.be.server.coupon.exception.UserCouponNotFoundException;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class UseCouponUseCase {

    private final UserCouponReader userCouponReader;
    private final OrderItemReader orderItemReader;

    private final UserCouponRepository userCouponRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserCouponDomainService userCouponDomainService;

    private final OrderItemMapper orderItemMapper;
    private final UserCouponMapper userCouponMapper;

    public void execute(PaymentCommand command) {

        OrderItem orderItem = orderItemReader.findOrderItemOrThrow(command.orderItemId());
        UserCoupon userCoupon = userCouponReader.findByCouponIdUserCouponOrThrow(command.couponId());

        userCouponDomainService.applyCoupon(orderItem, userCoupon);

        OrderItemEntity updateOrderItem = orderItemMapper.toEntity(orderItem);
        UserCouponEntity saveUserCoupon = userCouponMapper.toEntity(userCoupon);

        userCouponRepository.save(saveUserCoupon);
        orderItemRepository.save(updateOrderItem);
    }

}
