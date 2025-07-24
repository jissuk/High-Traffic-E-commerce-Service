package kr.hhplus.be.server.order.domain.mapper;

import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface OrderItemMapper {
    OrderItem toDomain(OrderItemEntity orderItemEntity);
    OrderItemEntity toEntity(OrderItem orderItem);
}

