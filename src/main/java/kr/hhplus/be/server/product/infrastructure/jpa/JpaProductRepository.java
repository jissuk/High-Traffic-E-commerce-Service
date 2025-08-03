package kr.hhplus.be.server.product.infrastructure.jpa;

import kr.hhplus.be.server.product.domain.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {

    ProductEntity findById(long id);
}
