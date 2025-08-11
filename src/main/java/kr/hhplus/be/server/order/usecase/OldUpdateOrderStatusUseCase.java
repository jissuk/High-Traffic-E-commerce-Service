//package kr.hhplus.be.server.order.usecase;
//
//import kr.hhplus.be.server.common.annotation.UseCase;
//import kr.hhplus.be.server.common.sender.OrderDataSender;
//import kr.hhplus.be.server.order.domain.model.*;
//import kr.hhplus.be.server.order.domain.repository.OrderRepository;
//import kr.hhplus.be.server.order.usecase.reader.OrderItemReader;
//import kr.hhplus.be.server.order.usecase.reader.OrderReader;
//import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
//import lombok.RequiredArgsConstructor;
//
//@UseCase
//@RequiredArgsConstructor
//public class UpdateOrderStatusUseCase {
//
//    private final OrderReader orderReader;
//    private final OrderItemReader orderItemReader;
//
//    private final OrderRepository orderRepositroy;
//
//    private final OrderDataSender orderDataSender;
//
//    public void execute(PaymentCommand command) {
//
//        Order order = orderReader.findOrderOrThrow(command.orderId());
//        order.complete();
//
//        orderRepositroy.save(order);
//
//        OrderItem orderItem = orderItemReader.findOrderItemOrThrow(command.orderItemId());
////        orderDataSender.send(orderItem);
//    }
//}