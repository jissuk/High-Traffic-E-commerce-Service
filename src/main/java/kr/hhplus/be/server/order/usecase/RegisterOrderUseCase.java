package kr.hhplus.be.server.order.usecase;

import kr.hhplus.be.server.common.annotation.DistributedLock;
import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.order.usecase.command.OrderCommand;
import kr.hhplus.be.server.payment.domain.Repository.PaymentRepository;
import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.support.TransactionTemplate;

@UseCase
@RequiredArgsConstructor
public class RegisterOrderUseCase {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private final TransactionTemplate transactionTemplate;

    @DistributedLock
    public void execute(OrderCommand command){
        Product product = productRepository.findById(command.productId());
        Coupon coupon = couponRepository.findById(command.couponId());
        User user = userRepository.findById(command.userId());

        transactionTemplate.executeWithoutResult(status -> completeOrder(command, product, user, coupon));
    }

    private void completeOrder(OrderCommand command, Product product, User user, Coupon coupon) {
        product.checkQuantity(command.quantity());
        user.deductPoint(command.point());

        Order order = createAndSaveOrder(command, product, coupon);
        createAndSaveOrderItem(command, order);
        createAndSavePayment(command, order);
    }

    private Order createAndSaveOrder(OrderCommand command, Product product, Coupon coupon) {

        Order order = Order.createPendingOrder(command, product, coupon);
        return orderRepository.save(order);
    }

    private void createAndSaveOrderItem(OrderCommand command, Order order){
        OrderItem orderItem = OrderItem.createBeforeOrderItem(command, order);
        orderItemRepository.save(orderItem);
    }

    private void createAndSavePayment(OrderCommand command, Order order){
        Payment payment = Payment.createBeforePayment(command, order);
        paymentRepository.save(payment);
    }
}
