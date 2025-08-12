package kr.hhplus.be.server.order.infrastructure;

import kr.hhplus.be.server.order.domain.mapper.OrderItemMapper;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.exception.OrderItemNotFoundException;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepository {

    private final JpaOrderItemRepository jpaOrderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderItem findById(long orderItemId) {

        return jpaOrderItemRepository.findById(orderItemId)
                .map(orderItemMapper::toDomain)
                .orElseThrow(OrderItemNotFoundException::new);
    }

    @Override
    public OrderItem save(OrderItem orderItem) {
        return orderItemMapper.toDomain(
                jpaOrderItemRepository.save(orderItemMapper.toEntity(orderItem))
        );
    }
}
