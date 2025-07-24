package kr.hhplus.be.server.order.infrastructure;

import kr.hhplus.be.server.order.domain.model.OrderHistoryEntity;
import kr.hhplus.be.server.order.domain.repository.OrderHistoryRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class OrderHistoryRepositoryImpl implements OrderHistoryRepository {

    private final Map<Long, OrderHistoryEntity> orderHistoryTable = new HashMap<>();

    @Override
    public OrderHistoryEntity save(OrderHistoryEntity orderHistory) {
        long sequence = 0L;
        sequence++;

        orderHistory.setId(sequence);
        return orderHistoryTable.put(sequence, orderHistory);
    }
}
