package kr.hhplus.be.server.product.infrastructure.jpa;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.product.domain.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findById(long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM ProductEntity p WHERE p.id = :productId")
    Optional<ProductEntity> findByIdForUpdate(long productId);

    @Query(value = """
        SELECT 
            pr.id,
            pr.product_name,
            pr.price,
            pr.quantity
        FROM payments p
        JOIN order_items oi ON p.order_item_id = oi.id
        JOIN products pr ON oi.product_id = pr.id
        WHERE p.create_at >= CURDATE() - INTERVAL 3 DAY
            AND p.create_at < CURDATE() 
            AND p.payment_status = 'COMPLETED'
        GROUP BY pr.id
        ORDER BY SUM(oi.quantity) DESC
        LIMIT 3
        """, nativeQuery = true)
    List<ProductEntity> findPopularProduct3Days();
}
