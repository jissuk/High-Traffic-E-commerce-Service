package kr.hhplus.be.server.order.domain.repository;

import kr.hhplus.be.server.order.domain.model.OrderHistoryEntity;

public interface OrderHistoryRepository {
    OrderHistoryEntity save(OrderHistoryEntity orderHistory);
}
