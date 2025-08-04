package kr.hhplus.be.server.order.usecase.reader;

import kr.hhplus.be.server.order.domain.mapper.OrderItemMapper;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.exception.OrderItemNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderItemReader {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    public OrderItem findOrderItemOrThrow(long id){
        OrderItemEntity orderItem = orderItemRepository.findById(id).orElseThrow(OrderItemNotFoundException::new);
        return orderItemMapper.toDomain(orderItem);
    }
}

