package kr.hhplus.be.server.order.infrastructure.jpa;

import kr.hhplus.be.server.order.domain.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaOrderRepositroy extends JpaRepository<OrderEntity, Integer> {

    Optional<OrderEntity> findById(long id);
}
