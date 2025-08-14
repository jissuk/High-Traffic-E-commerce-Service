package kr.hhplus.be.server.product.infrastructure;

import kr.hhplus.be.server.product.domain.mapper.ProductMapper;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.exception.ProductNotFoundException;
import kr.hhplus.be.server.product.infrastructure.jpa.JpaProductRepository;
import kr.hhplus.be.server.product.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;
    private final ProductMapper productMapper;

    @Override
    public Product findById(long productId) {
        return jpaProductRepository.findById(productId)
                .map(productMapper::toDomain)
                .orElseThrow(ProductNotFoundException::new);

    }

    @Override
    public Product findByIdForUpdate(long productId) {
        return jpaProductRepository.findByIdForUpdate(productId)
                .map(productMapper::toDomain)
                .orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public List<Product> findAll() {
        return jpaProductRepository.findAll()
                .stream().map(productMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Product save(Product product) {
        return productMapper.toDomain(
                jpaProductRepository.save(productMapper.toEntity(product))
        );
    }

    @Override
    public List<Object[]> findPopularProduct3Days() {
        return jpaProductRepository.findPopularProduct3Days();
    }
}
