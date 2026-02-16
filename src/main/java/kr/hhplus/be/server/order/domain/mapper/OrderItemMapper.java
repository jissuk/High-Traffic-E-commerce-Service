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
                        .orderId(orderItemEntity.getOrderId())
                        .productId(orderItemEntity.getProductId())
                        .build();
    }

    public OrderItemEntity toEntity(OrderItem orderItem){
        return OrderItemEntity.builder()
                                .id(orderItem.getId())
                                .quantity(orderItem.getQuantity())
                                .orderId(orderItem.getOrderId())
                                .productId(orderItem.getProductId())
                                .build();
    }
}

