package kr.hhplus.be.server.payment.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.hhplus.be.server.common.annotation.DistributedLock;
import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.coupon.domain.repository.UserCouponRepository;
import kr.hhplus.be.server.coupon.domain.service.UserCouponDomainService;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.payment.event.PaymentCompletedEvent;
import kr.hhplus.be.server.payment.domain.Repository.PaymentRepository;
import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.domain.service.PaymentDomainService;
import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.event.PointUseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.support.TransactionTemplate;


@UseCase
@RequiredArgsConstructor
public class RegisterPaymentUseCase {
    private final UserCouponDomainService userCouponDomainService;
    private final PaymentDomainService paymentDomainService;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final UserRepository userRepository;
    private final UserCouponRepository userCouponRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    private final TransactionTemplate transactionTemplate;

    @DistributedLock
    public void execute(PaymentCommand command) throws JsonProcessingException {

        Payment payment = paymentRepository.findById(command.paymentId());
        User user = userRepository.findById(command.userId());
        Order order = orderRepository.findById(command.orderId());
        OrderItem orderItem = orderItemRepository.findById(command.orderItemId());
        Product product = productRepository.findById(command.productId());

        transactionTemplate.executeWithoutResult(status -> {
            useCoupon(command, orderItem);
            usePoint(user, orderItem);
            processPayment(payment,order, orderItem, product);

            applicationEventPublisher.publishEvent(new PaymentCompletedEvent(
                    orderItem.getId(),
                    orderItem.getQuantity(),
                    orderItem.getPrice(),
                    orderItem.getTotalPrice(),
                    orderItem.getOrderId(),
                    orderItem.getProductId())
            );
        });
    }

    private void useCoupon(PaymentCommand command, OrderItem orderItem) {
        if (command.couponId() != null) {
            UserCoupon userCoupon = userCouponRepository.findByCouponId(command.couponId());

            userCouponDomainService.applyCoupon(orderItem, userCoupon);
            userCouponRepository.save(userCoupon);
        }
    }

    private void processPayment(Payment payment, Order order, OrderItem orderItem, Product product) {
        product.deductQuantity(orderItem.getQuantity());
        paymentDomainService.paymentComplate(payment, order);

        productRepository.save(product);
        paymentRepository.save(payment);
        orderRepository.save(order);
    }

    private void usePoint(User user, OrderItem orderItem) {
        user.deductPoint(orderItem.getTotalPrice());
        userRepository.save(user);
        applicationEventPublisher.publishEvent(PointUseEvent.from(user.getId(), orderItem.getTotalPrice()));
    }
}