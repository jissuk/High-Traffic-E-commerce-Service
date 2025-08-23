package kr.hhplus.be.server.payment.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.hhplus.be.server.common.annotation.DistributedLock;
import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.common.sender.OrderDataSender;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;


@UseCase
@RequiredArgsConstructor
public class RegisterPaymentUseCase {
    // 수정중1
    private final RedisTemplate<String, Long> redis;

    private final UserCouponDomainService userCouponDomainService;
    private final PaymentDomainService paymentDomainService;

    private final UserRepository userRepository;
    private final UserCouponRepository userCouponRepository;
    private final OrderItemRepository orderItemRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    private final TransactionTemplate transactionTemplate;

    private final OrderDataSender orderDataSender;

    public static final String PRODUCT_SALES_PREFIX = "product:sales:";

    // RedisKey
    @DistributedLock(key = "T(kr.hhplus.be.server.common.constant.RedisKey.Payment).LOCK_PAYMENT_REGISTER + #command.paymentId + ':lock'")
    @Transactional
    public void execute(PaymentCommand command) throws JsonProcessingException {

        Payment payment = paymentRepository.findById(command.paymentId());
        User user = userRepository.findById(command.userId());
        Order order = orderRepository.findById(command.orderId());
        OrderItem orderItem = orderItemRepository.findById(command.orderItemId());
        Product product = productRepository.findById(command.productId());

        useCoupon(command, orderItem);
        processPayment(payment, user, order, orderItem, product);

        recordProductSale(product, orderItem);

        //        orderDataSender.send(orderItem);
    }

    private void useCoupon(PaymentCommand command, OrderItem orderItem) {
        if (command.couponId() != null) {
            UserCoupon userCoupon = userCouponRepository.findByCouponId(command.couponId());

            userCouponDomainService.applyCoupon(orderItem, userCoupon);
            userCouponRepository.save(userCoupon);
        }
    }

    private void processPayment(Payment payment, User user, Order order, OrderItem orderItem, Product product) {
        user.deductPoint(orderItem.getTotalPrice());
        PointHistory pointHistory = PointHistory.use(user);

        product.deductQuantity(orderItem.getQuantity());

        paymentDomainService.paymentComplate(payment, order);

        userRepository.save(user);
        pointHistoryRepository.save(pointHistory);
        productRepository.save(product);
        paymentRepository.save(payment);
        orderRepository.save(order);
        orderItemRepository.save(orderItem);
    }

    private void recordProductSale(Product product, OrderItem orderItem) {
        String zSetKey = PRODUCT_SALES_PREFIX +LocalDate.now(ZoneId.of("Asia/Seoul"));
        redis.opsForZSet().incrementScore(zSetKey, product.getId(), orderItem.getQuantity());

        long baseTtlSeconds = TimeUnit.DAYS.toSeconds(5);

        redis.expire(zSetKey, baseTtlSeconds, TimeUnit.SECONDS);
    }

}