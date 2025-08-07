//package kr.hhplus.be.server.payment.usecase;
//
//import kr.hhplus.be.server.common.annotation.UseCase;
//import kr.hhplus.be.server.order.domain.model.OrderItem;
//import kr.hhplus.be.server.order.usecase.command.OrderItemCommand;
//import kr.hhplus.be.server.order.usecase.reader.OrderItemReader;
//import kr.hhplus.be.server.payment.domain.model.Payment;
//import kr.hhplus.be.server.payment.domain.Repository.PaymentRepository;
//import kr.hhplus.be.server.user.domain.model.User;
//import kr.hhplus.be.server.user.usecase.reader.UserReader;
//import lombok.RequiredArgsConstructor;
//
//@UseCase
//@RequiredArgsConstructor
//public class RegisterPaymentUseCase2 {
//
//    private final UserReader userReader;
//    private final OrderItemReader orderItemReader;
//
//    private final PaymentRepository paymentRepository;
//
//    public void execute(OrderItemCommand command) {
//
//        Payment payment = Payment.createBeforePayment(command);
//
//        User user = userReader.findUserOrThrow(command.userId());
//        OrderItem orderItem = orderItemReader.findOrderItemOrThrow(command.orderItemId());
//
//        payment.setUserId(user.getId());
//        payment.setOrderItemId(orderItem.getId());
//
//        paymentRepository.save(payment);
//    }
//}