package kr.hhplus.be.server.product.domain.repository;

import kr.hhplus.be.server.product.domain.model.ProductEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductRepository {
    Optional<ProductEntity> findById(long productId);
    List<ProductEntity> findAll();
    ProductEntity save(ProductEntity product);
}

