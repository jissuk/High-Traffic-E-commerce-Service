package kr.hhplus.be.server.payment.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.payment.domain.Repository.PaymentRepository;
import kr.hhplus.be.server.payment.domain.mapper.PaymentMapper;
import kr.hhplus.be.server.payment.domain.model.*;
import kr.hhplus.be.server.payment.exception.PaymentNotFoundException;
import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class UpdatePaymentStatusUseCase {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public void execute(PaymentCommand command) {
        PaymentEntity paymentEntity = findPaymentOrThrow(command.paymentId());
        Payment payment = paymentMapper.toDomain(paymentEntity);

        payment.complete();

        PaymentEntity updatePayment = paymentMapper.toEntity(payment);
        paymentRepository.update(updatePayment);
    }

    private PaymentEntity findPaymentOrThrow(long id) {
        PaymentEntity payment = paymentRepository.findById(id);
        if (payment == null) {
            throw new PaymentNotFoundException();
        }
        return payment;
    }
}
