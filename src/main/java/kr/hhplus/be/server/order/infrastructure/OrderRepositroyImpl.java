package kr.hhplus.be.server.order.infrastructure;

import kr.hhplus.be.server.order.domain.model.OrderEntity;
import kr.hhplus.be.server.order.domain.repository.OrderRepositroy;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class OrderRepositroyImpl implements OrderRepositroy {

    private final Map<Long, OrderEntity> orderTable = new HashMap<>();

    @Override
    public OrderEntity findById(long orderId) {
        return orderTable.get(orderId);
    }

    @Override
    public OrderEntity save(OrderEntity order) {
        long sequence = 0L;
        sequence++;
        return orderTable.put(sequence, order);
    }

    @Override
    public OrderEntity update(OrderEntity order) {
        return orderTable.put(order.getId(), order);
    }

    @Override
    public void clear() {
        orderTable.clear();
    }
}
