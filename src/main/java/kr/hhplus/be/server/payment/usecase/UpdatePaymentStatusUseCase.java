package kr.hhplus.be.server.payment.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.payment.domain.Repository.PaymentRepository;
import kr.hhplus.be.server.payment.domain.mapper.PaymentMapper;
import kr.hhplus.be.server.payment.domain.model.*;
import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
import kr.hhplus.be.server.payment.usecase.reader.PaymentReader;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class UpdatePaymentStatusUseCase {
    private final PaymentReader paymentReader;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public void execute(PaymentCommand command) {
        Payment payment = paymentReader.findPaymentOrThrow(command.paymentId());
        payment.complete();

        PaymentEntity updatePayment = paymentMapper.toEntity(payment);
        paymentRepository.save(updatePayment);
    }
}
