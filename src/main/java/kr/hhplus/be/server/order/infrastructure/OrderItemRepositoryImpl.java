package kr.hhplus.be.server.order.infrastructure;

import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderItemRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class OrderItemRepositoryImpl implements OrderItemRepository {

    private final JpaOrderItemRepository jpaOrderItemRepository;

    public OrderItemRepositoryImpl(JpaOrderItemRepository jpaOrderItemRepository) {
        this.jpaOrderItemRepository = jpaOrderItemRepository;
    }
    @Override
    public OrderItemEntity findById(long orderItemId) {

        return jpaOrderItemRepository.findById(orderItemId);
    }

    @Override
    public OrderItemEntity save(OrderItemEntity orderItem) {
        return jpaOrderItemRepository.save(orderItem);
    }
}
