package kr.hhplus.be.server.payment.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.payment.domain.Repository.PaymentHistoryRepository;
import kr.hhplus.be.server.payment.domain.Repository.PaymentRepository;
import kr.hhplus.be.server.payment.domain.mapper.PaymentHistoryMapper;
import kr.hhplus.be.server.payment.domain.mapper.PaymentMapper;
import kr.hhplus.be.server.payment.domain.model.*;
import kr.hhplus.be.server.payment.exception.PaymentNotFoundException;
import kr.hhplus.be.server.payment.usecase.dto.PaymentRequestDTO;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class UpdatePaymentStatusUseCase {

    private final PaymentRepository paymentRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentHistoryMapper paymentHistoryMapper;

    public void execute(PaymentRequestDTO request) {
        PaymentEntity paymentEntity = findPaymentOrThrow(request.getPaymentId());
        Payment payment = paymentMapper.toDomain(paymentEntity);

        payment.complete();
        PaymentHistory paymentHistory = PaymentHistory.of(payment);

        PaymentEntity updatePayment = paymentMapper.toEntity(payment);
        PaymentHistoryEntity saveHistory = paymentHistoryMapper.toEntity(paymentHistory);
        paymentRepository.update(updatePayment);
        paymentHistoryRepository.save(saveHistory);
    }

    private PaymentEntity findPaymentOrThrow(long id) {
        PaymentEntity payment = paymentRepository.findById(id);
        if (payment == null) {
            throw new PaymentNotFoundException();
        }
        return payment;
    }

    public void compensate(PaymentRequestDTO request) {

        PaymentEntity paymentEntity = findPaymentOrThrow(request.getUserId());

        Payment payment = paymentMapper.toDomain(paymentEntity);
        payment.beforePayment();

        PaymentEntity updatePayment = paymentMapper.toEntity(payment);
        paymentRepository.update(updatePayment);
        System.out.println("결제 상태 롤백 완료: paymentId=" + request.getPaymentId() + ", paymentStatus=" + payment.getPaymentStatus());
    }
}
