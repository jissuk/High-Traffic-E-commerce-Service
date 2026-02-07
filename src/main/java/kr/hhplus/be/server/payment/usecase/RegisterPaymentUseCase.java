package kr.hhplus.be.server.payment.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.common.annotation.DistributedLock;
import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.coupon.domain.repository.UserCouponRepository;
import kr.hhplus.be.server.coupon.domain.service.UserCouponDomainService;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.payment.domain.Repository.PaymentRepository;
import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.domain.service.PaymentDomainService;
import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.user.domain.model.PointHistory;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.repository.PointHistoryRepository;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;


@UseCase
@RequiredArgsConstructor
public class RegisterPaymentUseCase {
    private final UserCouponDomainService userCouponDomainService;
    private final PaymentDomainService paymentDomainService;

    private final KafkaTemplate<String, Object> kafka;
    private final TransactionTemplate transactionTemplate;

    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final UserCouponRepository userCouponRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    private final ObjectMapper objectMapper;

    private final static String PAYMENT_COMPLETE_TOPIC = "paymentComplete";

    @DistributedLock
    public void execute(PaymentCommand command) throws JsonProcessingException {
        Payment payment = paymentRepository.findByOrderItemId(command.orderItemId());
        User user = userRepository.findById(command.userId());
        Order order = orderRepository.findById(command.orderId());
        OrderItem orderItem = orderItemRepository.findById(command.orderItemId());
        Product product = productRepository.findById(command.productId());

        transactionTemplate.executeWithoutResult(status -> {
            completePayment(command, user, product, order, orderItem, payment);
            publishPaymentCompleteAfterCommit(orderItem);
        });
    }

    private void completePayment(PaymentCommand command, User user, Product product, Order order, OrderItem orderItem, Payment payment){
        useCoupon(command, orderItem);
        usePoint(user, orderItem);
        deductQuantity(product, orderItem);
        processPayment(order, payment);
    }

    private void useCoupon(PaymentCommand command, OrderItem orderItem) {
        if (command.couponId() != null) {
            UserCoupon userCoupon = userCouponRepository.findByCouponId(command.couponId());

            userCouponDomainService.applyCoupon(orderItem, userCoupon);
            userCouponRepository.save(userCoupon);
        }
    }

    private void usePoint(User user, OrderItem orderItem) {
        user.deductPoint(orderItem.getTotalPrice());
        PointHistory pointHistory = PointHistory.use(user);

        userRepository.save(user);
        pointHistoryRepository.save(pointHistory);
    }

    private void deductQuantity(Product product, OrderItem orderItem) {
        product.deductQuantity(orderItem.getQuantity());

        productRepository.save(product);
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