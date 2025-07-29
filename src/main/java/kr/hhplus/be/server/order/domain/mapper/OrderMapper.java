package kr.hhplus.be.server.order.domain.mapper;

import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toDomain(OrderEntity orderEntity);
    OrderEntity toEntity(Order order);
}



