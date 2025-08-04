package kr.hhplus.be.server.order.infrastructure;

import kr.hhplus.be.server.order.domain.model.OrderEntity;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderRepositroy;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class OrderRepositroyImpl implements OrderRepository {

    private final JpaOrderRepositroy jpaOrderRepositroy;

    public OrderRepositroyImpl(JpaOrderRepositroy jpaOrderRepositroy) {
        this.jpaOrderRepositroy = jpaOrderRepositroy;
    }

    @Override
    public Optional<OrderEntity> findById(long orderId) {

        return jpaOrderRepositroy.findById(orderId);
    }

    @Override
    public OrderEntity save(OrderEntity order) {
        return jpaOrderRepositroy.save(order);
    }

}
