package kr.hhplus.be.server.payment.usecase.reader;

import kr.hhplus.be.server.payment.domain.Repository.PaymentRepository;
import kr.hhplus.be.server.payment.domain.mapper.PaymentMapper;
import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.domain.model.PaymentEntity;
import kr.hhplus.be.server.payment.exception.PaymentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentReader {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public Payment findPaymentOrThrow(long id){
        PaymentEntity paymentEntity = paymentRepository.findById(id).orElseThrow(PaymentNotFoundException::new);
        return paymentMapper.toDomain(paymentEntity);
    }
}
