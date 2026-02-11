package kr.hhplus.be.server.order.domain.repository;

import kr.hhplus.be.server.order.domain.model.OrderItem;

public interface OrderItemRepository {
    OrderItem findById(long orderItemId);
    OrderItem save(OrderItem orderItem);
}
