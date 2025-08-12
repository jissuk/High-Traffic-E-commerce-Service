package kr.hhplus.be.server.payment.usecase;

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


@UseCase
@RequiredArgsConstructor
public class RegisterPaymentUseCase {

    private final RedisTemplate<String, Object> redis;

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

    @Transactional
    public void execute(PaymentCommand command){

        Payment payment = paymentRepository.findById(command.paymentId());
        User user = userRepository.findById(command.userId());
        OrderItem orderItem = orderItemRepository.findById(command.orderItemId());
        Order order = orderRepository.findById(command.orderId());
        Product product = productRepository.findByIdForUpdate(command.productId());

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

        redis.opsForZSet().incrementScore("sales:"+ LocalDate.now(), 4, 1);

        //        orderDataSender.send(orderItem);

    }
}