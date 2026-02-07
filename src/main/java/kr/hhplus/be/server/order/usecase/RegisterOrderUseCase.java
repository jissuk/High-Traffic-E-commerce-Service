package kr.hhplus.be.server.order.usecase;

import kr.hhplus.be.server.common.annotation.DistributedLock;
import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.order.usecase.command.OrderItemCommand;
import kr.hhplus.be.server.order.usecase.dto.OrderItemResponse;
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
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private final TransactionTemplate transactionTemplate;

    @DistributedLock
    public OrderItemResponse execute(OrderItemCommand command){
        User user = userRepository.findById(command.userId());
        Product product = productRepository.findById(command.productId());

        OrderItem orderItem = transactionTemplate.execute(status -> completeOrder(command, user, product));

        return OrderItemResponse.from(orderItem);
    }

    private OrderItem completeOrder(OrderItemCommand command, User user, Product product) {
        deductProductQuantity(product, command);
        Order order = createAndSaveOrder(user);
        OrderItem orderItem = createAndSaveOrderItem(command, order);
        createAndSavePayment(command, orderItem);
        return orderItem;
    }

    private void deductProductQuantity(Product product, OrderItemCommand command){
        product.deductQuantity(command.quantity());
        productRepository.save(product);
    }

    private Order createAndSaveOrder(User user){
        Order order = Order.createBeforeOrder(user);
        return orderRepository.save(order);
    }
    private OrderItem createAndSaveOrderItem(OrderItemCommand command, Order order){
        OrderItem orderItem = OrderItem.createBeforeOrderItem(command, order);
        return orderItemRepository.save(orderItem);
    }
    private void createAndSavePayment(OrderItemCommand command, OrderItem orderItem){
        Payment payment = Payment.createBeforePayment(command, orderItem);
        paymentRepository.save(payment);
    }
}
