package kr.hhplus.be.server.product.infrastructure;

import kr.hhplus.be.server.product.domain.mapper.ProductMapper;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.exception.ProductNotFoundException;
import kr.hhplus.be.server.product.infrastructure.jpa.JpaProductRepository;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.usecase.dto.ProductResponse;
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
    public Product save(Product product) {
        return productMapper.toDomain(
                jpaProductRepository.save(productMapper.toEntity(product))
        );
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = jpaProductRepository.findAll()
                .stream()
                .map(productMapper::toDomain)
                .toList();

        if (products.isEmpty()) {
            throw new ProductNotFoundException();
        }

        return products;
    }

    @Override
    public List<Product> findPopularProduct3Days() {
        List<Product> products = jpaProductRepository.findPopularProduct3Days()
                .stream()
                .map(productMapper::toDomain)
                .toList();

        if (products.isEmpty()) {
            throw new ProductNotFoundException();
        }

        return products;
    }
}
