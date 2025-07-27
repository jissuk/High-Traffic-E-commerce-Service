package kr.hhplus.be.server.payment.domain.mapper;

import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.domain.model.PaymentEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface PaymentMapper {
    Payment toDomain(PaymentEntity paymentEntity);

    PaymentEntity toEntity(Payment payment);
}

