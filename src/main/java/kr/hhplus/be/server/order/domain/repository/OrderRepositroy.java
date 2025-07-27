package kr.hhplus.be.server.order.domain.repository;


import kr.hhplus.be.server.order.domain.model.OrderEntity;

public interface OrderRepositroy {
    OrderEntity findById(long orderId);
    OrderEntity save(OrderEntity order);
    OrderEntity update(OrderEntity order);
    void clear();
}
