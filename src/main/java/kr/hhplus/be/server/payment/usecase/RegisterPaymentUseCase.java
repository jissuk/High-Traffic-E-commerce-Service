package kr.hhplus.be.server.payment.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.usecase.command.OrderItemCommand;
import kr.hhplus.be.server.order.usecase.reader.OrderItemReader;
import kr.hhplus.be.server.payment.domain.mapper.PaymentMapper;
import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.domain.model.PaymentEntity;
import kr.hhplus.be.server.payment.domain.Repository.PaymentRepository;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.usecase.reader.UserReader;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class RegisterPaymentUseCase {

    private final UserReader userReader;
    private final OrderItemReader orderItemReader;

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public void execute(OrderItemCommand command) {

        Payment payment = Payment.createBeforePayment(command);
        PaymentEntity saveFayment = paymentMapper.toEntity(payment);

        User user = userReader.findUserOrThrow(command.userId());
        OrderItem orderItem = orderItemReader.findOrderItemOrThrow(command.orderItemId());

        saveFayment.setUserId(user.getId());
        saveFayment.setOrderItemId(orderItem.getId());

        paymentRepository.save(saveFayment);
    }
}