package kr.hhplus.be.server.order.infrastructure.jpa;

import kr.hhplus.be.server.order.domain.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaOrderItemRepository extends JpaRepository<OrderItem, Integer> {
    Optional<OrderItem> findById(long id);
}
