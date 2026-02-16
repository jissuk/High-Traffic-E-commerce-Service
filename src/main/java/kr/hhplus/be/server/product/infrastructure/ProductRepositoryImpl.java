package kr.hhplus.be.server.product.infrastructure;

import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.exception.ProductNotFoundException;
import kr.hhplus.be.server.product.infrastructure.jpa.JpaProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;

    @Override
    public Product findById(long productId) {
        return jpaProductRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public Product findByIdForUpdate(long productId) {
        return jpaProductRepository.findByIdForUpdate(productId)
                .orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public Product save(Product product) {
        return jpaProductRepository.save((product));
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = jpaProductRepository.findAll()
                                                        .stream()
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
                                                        .toList();
        if (products.isEmpty()) {
            throw new ProductNotFoundException();
        }
        return products;
    }
}
