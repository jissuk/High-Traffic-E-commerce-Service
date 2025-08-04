package kr.hhplus.be.server.order.usecase.reader;

import kr.hhplus.be.server.order.domain.mapper.OrderMapper;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderEntity;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.order.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderReader {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public Order findOrderOrThrow(long id){
        OrderEntity orderEntity = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
        return orderMapper.toDomain(orderEntity);
    }
}
