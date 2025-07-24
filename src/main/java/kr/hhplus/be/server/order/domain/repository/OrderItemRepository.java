package kr.hhplus.be.server.order.domain.repository;

import kr.hhplus.be.server.order.domain.model.OrderItemEntity;

public interface OrderItemRepository {
    OrderItemEntity findById(long orderItemId);
    OrderItemEntity save(OrderItemEntity orderItem);
    OrderItemEntity update(OrderItemEntity orderItem);
    void clear();
}
