package kr.hhplus.be.server.product.domain.repository;

import kr.hhplus.be.server.product.domain.model.Product;

import java.util.List;

public interface ProductRepository {
    Product findById(long productId);
    Product findByIdForUpdate(long productId);
    List<Product> findAll();
    Product save(Product product);
    List<Product> findPopularProduct3Days();
}

