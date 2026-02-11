package kr.hhplus.be.server.order.infrastructure.jpa;

import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaOrderItemRepository extends JpaRepository<OrderItemEntity, Integer> {
    Optional<OrderItemEntity> findById(long id);
    Optional<OrderItemEntity> findByOrderId(long orderId);
}
