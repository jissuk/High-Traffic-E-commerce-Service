package kr.hhplus.be.server.order.domain.repository;


import kr.hhplus.be.server.order.domain.model.Order;

import java.util.Optional;

public interface OrderRepository {
    Order findById(long orderId);
    Order save(Order order);
}
