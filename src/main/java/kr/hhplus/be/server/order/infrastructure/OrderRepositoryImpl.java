package kr.hhplus.be.server.order.infrastructure;

import kr.hhplus.be.server.order.domain.mapper.OrderMapper;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.order.exception.OrderNotFoundException;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;
    private final OrderMapper orderMapper;

    @Override
    public Order findById(long orderId) {

        return jpaOrderRepository.findById(orderId)
                .map(orderMapper::toDomain)
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public Order save(Order order){
        return orderMapper.toDomain(
                jpaOrderRepository.save(orderMapper.toEntity(order))
        );
    }
}
