package kr.hhplus.be.server.payment.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.exception.OrderItemNotFoundException;
import kr.hhplus.be.server.order.usecase.command.OrderItemCommand;
import kr.hhplus.be.server.order.usecase.dto.OrderItemRequestDTO;
import kr.hhplus.be.server.payment.domain.Repository.PaymentHistoryRepository;
import kr.hhplus.be.server.payment.domain.mapper.PaymentHistoryMapper;
import kr.hhplus.be.server.payment.domain.mapper.PaymentMapper;
import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.domain.model.PaymentEntity;
import kr.hhplus.be.server.payment.domain.Repository.PaymentRepository;
import kr.hhplus.be.server.payment.domain.model.PaymentHistory;
import kr.hhplus.be.server.payment.domain.model.PaymentHistoryEntity;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class RegisterPaymentUseCase {

    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentHistoryRepository  paymentHistoryRepository;

    private final PaymentMapper paymentMapper;
    private final PaymentHistoryMapper paymentHistoryMapper;

    public void execute(OrderItemCommand command) {

        Payment payment = Payment.createBeforePayment(command);
        PaymentHistory paymentHistory = PaymentHistory.of(payment);

        PaymentEntity saveFayment = paymentMapper.toEntity(payment);
        PaymentHistoryEntity saveHistory = paymentHistoryMapper.toEntity(paymentHistory);

        UserEntity user = findUserOrThrow(command.userId());
        OrderItemEntity orderItem = findOrderItemOrThrow(command.orderItemId());
        saveFayment.setUser(user);
        saveFayment.setOrderItem(orderItem);

        paymentRepository.save(saveFayment);
        paymentHistoryRepository.save(saveHistory);
    }

    private UserEntity findUserOrThrow(long id) {
        UserEntity user = userRepository.findById(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    private OrderItemEntity findOrderItemOrThrow(long id) {
        OrderItemEntity orderItem = orderItemRepository.findById(id);
        if (orderItem == null) {
            throw new OrderItemNotFoundException();
        }
        return orderItem;
    }
}
