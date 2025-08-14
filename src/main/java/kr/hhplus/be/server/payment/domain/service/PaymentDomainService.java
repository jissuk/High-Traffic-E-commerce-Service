package kr.hhplus.be.server.payment.domain.service;

import kr.hhplus.be.server.common.annotation.DomainService;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.payment.domain.model.Payment;

@DomainService
public class PaymentDomainService {

    public void paymentComplate(Payment payment, Order order) {
        payment.checkPayment();
        payment.complete();
        order.checkNotCompletedOrder();
        order.complete();
    }
}
