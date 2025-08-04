package kr.hhplus.be.server.product.infrastructure;

import kr.hhplus.be.server.product.domain.model.ProductEntity;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.infrastructure.jpa.JpaProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;

    public ProductRepositoryImpl(JpaProductRepository jpaProductRepository) {
        this.jpaProductRepository = jpaProductRepository;
    }

    @Override
    public ProductEntity save(ProductEntity product) {
        return jpaProductRepository.save(product);
    }

    @Override
    public Optional<ProductEntity> findById(long productId) {
        return jpaProductRepository.findById(productId);

    }

    @Override
    public List<ProductEntity> findAll() {
        return jpaProductRepository.findAll();
    }
}
