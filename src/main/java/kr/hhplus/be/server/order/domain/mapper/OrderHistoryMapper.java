package kr.hhplus.be.server.order.domain.mapper;

import kr.hhplus.be.server.order.domain.model.OrderHistory;
import kr.hhplus.be.server.order.domain.model.OrderHistoryEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface OrderHistoryMapper {
    OrderHistory toDomain(OrderHistoryEntity orderHistoryEntity);
    OrderHistoryEntity toEntity(OrderHistory orderHistory);
}

