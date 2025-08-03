package kr.hhplus.be.server.order.domain.repository;


import kr.hhplus.be.server.order.domain.model.OrderEntity;

public interface OrderRepository {
    OrderEntity findById(long orderId);
    OrderEntity save(OrderEntity order);
}
