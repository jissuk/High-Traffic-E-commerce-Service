package kr.hhplus.be.server.order.domain.repository;

import kr.hhplus.be.server.order.domain.model.Order;

public interface OrderRepository {
    Order findById(long orderId);
    Order save(Order order);
}
