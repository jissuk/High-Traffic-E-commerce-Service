package kr.hhplus.be.server.product.domain.repository;

import kr.hhplus.be.server.product.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(long productId);
    Optional<Product> findByIdForUpdate(long productId);
    List<Product> findAll();
    Product save(Product product);
}

