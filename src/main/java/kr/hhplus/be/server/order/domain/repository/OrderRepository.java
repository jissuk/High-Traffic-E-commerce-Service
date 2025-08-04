package kr.hhplus.be.server.order.domain.repository;


import kr.hhplus.be.server.order.domain.model.OrderEntity;

import java.util.Optional;

public interface OrderRepository {
    Optional<OrderEntity> findById(long orderId);
    OrderEntity save(OrderEntity order);
}
