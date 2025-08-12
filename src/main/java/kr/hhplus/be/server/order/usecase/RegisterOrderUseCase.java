package kr.hhplus.be.server.order.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.order.usecase.command.OrderItemCommand;
import kr.hhplus.be.server.payment.domain.Repository.PaymentRepository;
import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.exception.ProductNotFoundException;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class RegisterOrderUseCase {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepositroy;
    private final OrderItemRepository orderItemRepository;


    /*
    * 1. 주문 등록
    * 2. 상품 수량 체크
    * 3. 주문 상세 등록
    * 4. 결제 등록
    * */
    public void execute(OrderItemCommand command){
        User user = userRepository.findById(command.userId());
        Product product = productRepository.findById(command.productId());

        Order order = Order.createBeforeOrder(user);
        orderRepositroy.save(order);

        product.checkQuantity(command.quantity());

        OrderItem orderItem = OrderItem.createBeforeOrderItem(command);
        orderItem.setProductId(product.getId());
        orderItem.setOrderId(order.getId());

        orderItemRepository.save(orderItem);

        Payment payment = Payment.createBeforePayment(command);
        payment.setUserId(user.getId());
        payment.setOrderItemId(orderItem.getId());

        paymentRepository.save(payment);
    }
}
