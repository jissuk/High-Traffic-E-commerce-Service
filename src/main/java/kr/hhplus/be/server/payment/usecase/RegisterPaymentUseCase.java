package kr.hhplus.be.server.payment.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


@UseCase
@RequiredArgsConstructor
public class RegisterPaymentUseCase {

    private final RedisTemplate<String, Object> redis;
    private final ObjectMapper objectMapper;

    private final UserCouponDomainService userCouponDomainService;
    private final PaymentDomainService paymentDomainService;

    private final UserRepository userRepository;
    private final UserCouponRepository userCouponRepository;
    private final OrderItemRepository orderItemRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    private final OrderDataSender orderDataSender;

    @DistributedLock(key = "RegisterPayment")
    @Transactional
    public void execute(PaymentCommand command) throws JsonProcessingException {

        Payment payment = paymentRepository.findById(command.paymentId());
        User user = userRepository.findById(command.userId());
        OrderItem orderItem = orderItemRepository.findById(command.orderItemId());
        Order order = orderRepository.findById(command.orderId());
        Product product = productRepository.findById(command.productId());

        if (command.couponId() != null) {
            UserCoupon userCoupon = userCouponRepository.findByCouponId(command.couponId());

            userCouponDomainService.applyCoupon(orderItem, userCoupon);

            orderItem = orderItemRepository.save(orderItem);
            userCouponRepository.save(userCoupon);
        }

        user.deductPoint(orderItem.getTotalPrice());
        PointHistory pointHistory = PointHistory.use(user);

        product.deductQuantity(orderItem.getQuantity());

        paymentDomainService.paymentComplate(payment, order);

        userRepository.save(user);
        pointHistoryRepository.save(pointHistory);
        productRepository.save(product);
        paymentRepository.save(payment);
        orderRepository.save(order);

        String zsetKey = "sales:" + LocalDate.now();
        String hashKey = "PopularProduct";

        // productId(pk), product
        redis.opsForHash().put(hashKey, product.getId(), objectMapper.writeValueAsString(product));
        redis.opsForZSet().incrementScore(zsetKey, product.getId(), orderItem.getQuantity());

        long baseTtlSeconds = TimeUnit.DAYS.toSeconds(5);  // 5일

        redis.expire(zsetKey, baseTtlSeconds, TimeUnit.SECONDS);
        redis.expire(hashKey, baseTtlSeconds, TimeUnit.SECONDS);

        //        orderDataSender.send(orderItem);
    }
}