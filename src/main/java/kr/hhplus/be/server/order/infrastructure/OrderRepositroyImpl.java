package kr.hhplus.be.server.order.infrastructure;

import kr.hhplus.be.server.order.domain.mapper.OrderMapper;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderEntity;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.order.exception.OrderNotFoundException;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositroyImpl implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepositroy;
    private final OrderMapper orderMapper;


    @Override
    public Order findById(long orderId) {

        return jpaOrderRepositroy.findById(orderId)
                .map(orderMapper::toDomain)
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public Order save(Order order){
        return orderMapper.toDomain(
                jpaOrderRepositroy.save(orderMapper.toEntity(order))
        );
    }

}
