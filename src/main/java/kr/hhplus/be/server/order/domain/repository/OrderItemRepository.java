package kr.hhplus.be.server.order.domain.repository;

import kr.hhplus.be.server.order.domain.model.OrderItem;

import java.util.Optional;

public interface OrderItemRepository {
    Optional<OrderItem> findById(long orderItemId);
    OrderItem save(OrderItem orderItem);
}
