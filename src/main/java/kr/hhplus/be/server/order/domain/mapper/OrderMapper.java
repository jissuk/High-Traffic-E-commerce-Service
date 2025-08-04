package kr.hhplus.be.server.order.domain.mapper;

import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public Order toDomain(OrderEntity orderEntity){
        return Order.builder()
                    .id(orderEntity.getId())
                    .orderStatus(orderEntity.getOrderStatus())
                    .createdAt(orderEntity.getCreatedAt())
                    .build();
    };

    public OrderEntity toEntity(Order order){
        return OrderEntity.builder()
                            .id(order.getId())
                            .orderStatus(order.getOrderStatus())
                            .createdAt(order.getCreatedAt())
                            .build();
    };

}



