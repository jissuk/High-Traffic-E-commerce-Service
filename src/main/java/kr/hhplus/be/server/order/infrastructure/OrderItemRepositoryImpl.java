package kr.hhplus.be.server.order.infrastructure;

import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class OrderItemRepositoryImpl implements OrderItemRepository {

    private final Map<Long, OrderItemEntity> orderItemTable = new HashMap<>();

    @Override
    public OrderItemEntity findById(long orderItemId) {
        return orderItemTable.get(orderItemId);
    }

    @Override
    public OrderItemEntity save(OrderItemEntity orderItem) {
        long sequence = 0L;
        sequence++;

        orderItem.setId(sequence);
        return orderItemTable.put(orderItem.getId(), orderItem);
    }

    @Override
    public OrderItemEntity update(OrderItemEntity orderItem) {
        return orderItemTable.put(orderItem.getId(), orderItem);
    }

    @Override
    public void clear() {
        orderItemTable.clear();
    }
}
