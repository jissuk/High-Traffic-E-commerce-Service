package kr.hhplus.be.server.coupon.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.coupon.domain.mapper.UserCouponMapper;
import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.coupon.domain.model.UserCouponEntity;
import kr.hhplus.be.server.order.domain.mapper.OrderItemMapper;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.exception.OrderItemNotFoundException;
import kr.hhplus.be.server.payment.usecase.dto.PaymentRequestDTO;
import kr.hhplus.be.server.user.domain.repository.UserCouponRepository;
import kr.hhplus.be.server.coupon.domain.service.UserCouponDomainService;
import kr.hhplus.be.server.coupon.exception.UserCouponNotFoundException;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class UseCouponUseCase {

    private final UserCouponRepository userCouponRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserCouponDomainService userCouponDomainService;

    private final OrderItemMapper orderItemMapper;
    private final UserCouponMapper userCouponMapper;

    public void execute(PaymentRequestDTO request) {

        OrderItemEntity orderItemEntity = findOrderItemOrThrow(request.getOrderItemId());
        UserCouponEntity userCouponEntity = findUserCouponOrThrow(request.getCouponId());

        OrderItem orderItem = orderItemMapper.toDomain(orderItemEntity);
        UserCoupon userCoupon = userCouponMapper.toDomain(userCouponEntity);

        userCouponDomainService.applyCoupon(orderItem, userCoupon);

        OrderItemEntity updateOrderItem = orderItemMapper.toEntity(orderItem);
        UserCouponEntity saveUserCoupon = userCouponMapper.toEntity(userCoupon);

        orderItemRepository.update(updateOrderItem);
        userCouponRepository.save(saveUserCoupon);
    }


    private OrderItemEntity findOrderItemOrThrow(long id) {
        OrderItemEntity orderItem = orderItemRepository.findById(id);
        if (orderItem == null) {
            throw new OrderItemNotFoundException();
        }
        return orderItem;
    }

    private UserCouponEntity findUserCouponOrThrow(long id) {
        UserCouponEntity userCoupon = userCouponRepository.findById(id);
        if (userCoupon == null) {
            throw new UserCouponNotFoundException();
        }
        return userCoupon;
    }

    public void compensate(PaymentRequestDTO request) {

        OrderItemEntity orderItemEntity = findOrderItemOrThrow(request.getOrderItemId());
        UserCouponEntity userCouponEntity = findUserCouponOrThrow(request.getCouponId());

        OrderItem orderItem = orderItemMapper.toDomain(orderItemEntity);
        UserCoupon userCoupon = userCouponMapper.toDomain(userCouponEntity);

        userCouponDomainService.withdrawCoupon(orderItem, userCoupon);

        OrderItemEntity updateOrderItem = orderItemMapper.toEntity(orderItem);
        UserCouponEntity saveUserCoupon = userCouponMapper.toEntity(userCoupon);

        orderItemRepository.update(updateOrderItem);
        userCouponRepository.save(saveUserCoupon);
        System.out.println("쿠폰 사용 롤백 완료: couponId=" + request.getCouponId());
    }
}
