package kr.hhplus.be.server.payment.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.hhplus.be.server.common.annotation.DistributedLock;
import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.coupon.domain.repository.UserCouponRepository;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.payment.domain.Repository.PaymentRepository;
import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;


@UseCase
@RequiredArgsConstructor
public class RegisterPaymentUseCase {
    private final KafkaTemplate<String, Object> kafka;
    private final TransactionTemplate transactionTemplate;

    private final UserCouponRepository userCouponRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    private final static String PAYMENT_COMPLETE_TOPIC = "paymentComplete";

    @DistributedLock
    public void execute(PaymentCommand command){
        Payment payment = paymentRepository.findByOrderItemId(command.orderItemId());
        Order order = orderRepository.findById(command.orderId());
        OrderItem orderItem = orderItemRepository.findById(command.orderItemId());

        transactionTemplate.executeWithoutResult(status -> {
            completePayment(command,order, orderItem, payment);
            publishPaymentCompleteAfterCommit(orderItem);
        });
    }

    private void completePayment(PaymentCommand command, Order order, OrderItem orderItem, Payment payment){
        useCoupon(command, orderItem);
        processPayment(order, payment);
    }

    private void useCoupon(PaymentCommand command, OrderItem orderItem) {
        if (command.couponId() != null) {
            UserCoupon userCoupon = userCouponRepository.findByCouponId(command.couponId());

            userCouponDomainService.applyCoupon(orderItem, userCoupon);
            userCouponRepository.save(userCoupon);
        }
    }

    private void processPayment(Order order, Payment payment) {
        paymentDomainService.paymentComplete(order, payment);
        orderRepository.save(order);
        paymentRepository.save(payment);
    }

    private void publishPaymentCompleteAfterCommit(OrderItem orderItem) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                kafka.send(PAYMENT_COMPLETE_TOPIC,orderItem);
            }
        });
    }
}