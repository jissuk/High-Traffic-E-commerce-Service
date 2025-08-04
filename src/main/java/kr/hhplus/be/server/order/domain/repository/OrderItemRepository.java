package kr.hhplus.be.server.order.domain.repository;

import kr.hhplus.be.server.order.domain.model.OrderItemEntity;

import java.util.Optional;

public interface OrderItemRepository {
    Optional<OrderItemEntity> findById(long orderItemId);
    OrderItemEntity save(OrderItemEntity orderItem);
}
