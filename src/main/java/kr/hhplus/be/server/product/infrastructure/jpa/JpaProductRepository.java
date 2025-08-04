package kr.hhplus.be.server.product.infrastructure.jpa;

import kr.hhplus.be.server.product.domain.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findById(long id);
}
