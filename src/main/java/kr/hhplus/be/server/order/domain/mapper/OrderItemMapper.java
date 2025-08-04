package kr.hhplus.be.server.order.domain.mapper;

import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    public OrderItem toDomain(OrderItemEntity orderItemEntity){
        return OrderItem.builder()
                        .id(orderItemEntity.getId())
                        .quantity(orderItemEntity.getQuantity())
                        .price(orderItemEntity.getPrice())
                        .totalPrice(orderItemEntity.getTotalPrice())
                        .build();
    }

    public OrderItemEntity toEntity(OrderItem orderItem){
        return OrderItemEntity.builder()
                                .id(orderItem.getId())
                                .quantity(orderItem.getQuantity())
                                .price(orderItem.getPrice())
                                .totalPrice(orderItem.getTotalPrice())
                                .build();
    }
}

