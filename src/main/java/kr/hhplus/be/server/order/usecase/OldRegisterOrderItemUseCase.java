//package kr.hhplus.be.server.order.usecase;
//
//import kr.hhplus.be.server.common.annotation.UseCase;
//import kr.hhplus.be.server.order.domain.model.Order;
//import kr.hhplus.be.server.order.domain.model.OrderItem;
//import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
//import kr.hhplus.be.server.order.usecase.command.OrderItemCommand;
//import kr.hhplus.be.server.order.usecase.reader.OrderReader;
//import kr.hhplus.be.server.product.domain.model.Product;
//import kr.hhplus.be.server.product.usecase.reader.ProductReader;
//import lombok.RequiredArgsConstructor;
//
//@UseCase
//@RequiredArgsConstructor
//public class RegisterOrderItemUseCase {
//    private final ProductReader productReader;
//    private final OrderReader orderReader;
//
//    private final OrderItemRepository orderItemRepository;
//
//
//    public void execute(OrderItemCommand command) {
//        Product product = productReader.findProductOrThrow(command.productId());
//        Order order = orderReader.findOrderOrThrow(command.orderId());
//
//        OrderItem orderItem = OrderItem.createBeforeOrderItem(command);
//
//        orderItem.setProductId(product.getId());
//        orderItem.setOrderId(order.getId());
//
//        orderItemRepository.save(orderItem);
//
//    }
//}
