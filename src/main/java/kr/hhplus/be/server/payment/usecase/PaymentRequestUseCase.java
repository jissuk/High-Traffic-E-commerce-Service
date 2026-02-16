package kr.hhplus.be.server.payment.usecase;

import kr.hhplus.be.server.common.annotation.DistributedLock;
import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.common.outbox.service.OutboxService;

import kr.hhplus.be.server.payment.domain.Repository.PaymentRepository;
import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.event.PaymentRequestEvent;
import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class PaymentRequestUseCase {
    private final OutboxService outboxService;
    private final PaymentRepository paymentRepository;

    @Transactional
    @DistributedLock
    public void execute(PaymentCommand command){
        Payment payment = paymentRequested(command);
        publishRequestPaymentEvent(payment);
    }

    private Payment paymentRequested(PaymentCommand command) {
        Payment payment = paymentRepository.findByOrderId(command.orderId());
        payment.validateTossPaymentConsistency(command.amount());
        payment.requested();

        return payment;
    }

    private void publishRequestPaymentEvent(Payment payment){
        PaymentRequestEvent event = PaymentRequestEvent.of(payment);
        outboxService.save(event);
    }
}