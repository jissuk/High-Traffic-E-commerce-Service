package kr.hhplus.be.server.product.domain.repository;

import kr.hhplus.be.server.product.domain.model.ProductEntity;

import java.util.List;
import java.util.Map;

public interface ProductRepository {
    ProductEntity save(ProductEntity product);
    ProductEntity findById(long productId);
    List<ProductEntity> findAll();
}

