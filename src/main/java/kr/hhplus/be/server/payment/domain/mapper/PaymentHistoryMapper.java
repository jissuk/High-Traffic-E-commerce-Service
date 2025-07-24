package kr.hhplus.be.server.payment.domain.mapper;

import kr.hhplus.be.server.payment.domain.model.PaymentHistory;
import kr.hhplus.be.server.payment.domain.model.PaymentHistoryEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface PaymentHistoryMapper {
    PaymentHistory toDomain(PaymentHistoryEntity paymentHistoryEntity);
    PaymentHistoryEntity toEntity(PaymentHistory paymentHistory);
}

