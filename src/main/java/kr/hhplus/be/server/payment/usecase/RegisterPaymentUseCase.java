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
import kr.hhplus.be.server.payment.event.PaymentCompletedEvent;
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

    private final KafkaTemplate<String, String> kafka;
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

        Payment payment = paymentRepository.findById(command.paymentId());
        User user = userRepository.findById(command.userId());
        Order order = orderRepository.findById(command.orderId());
        OrderItem orderItem = orderItemRepository.findById(command.orderItemId());
        Product product = productRepository.findById(command.productId());

        String jsonOrderItem = objectMapper.writeValueAsString(orderItem);

        transactionTemplate.executeWithoutResult(status -> {
            useCoupon(command, orderItem);
            usePoint(user, orderItem);
            processPayment(payment,order, orderItem, product);

            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    kafka.send(PAYMENT_COMPLETE_TOPIC,jsonOrderItem);
                }
            });
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
        PointHistory pointHistory = PointHistory.use(user);
        pointHistoryRepository.save(pointHistory);
    }
}