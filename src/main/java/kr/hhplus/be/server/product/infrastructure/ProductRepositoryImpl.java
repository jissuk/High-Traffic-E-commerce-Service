package kr.hhplus.be.server.product.infrastructure;

import kr.hhplus.be.server.product.domain.model.ProductEntity;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final Map<Long, ProductEntity> productTable = new HashMap<>();

    @Override
    public ProductEntity save(ProductEntity product) {
        long sequence = 0L;
        sequence++;
        return productTable.put(sequence, product);
    }

    @Override
    public ProductEntity findById(long productId) {
        return productTable.get(productId);
    }

    @Override
    public ProductEntity update(ProductEntity product) {

        return productTable.put(product.getId(), product);
    }

    @Override
    public Map<Long, ProductEntity> findAll() {
        return productTable;
    }

    @Override
    public void clear() {
        productTable.clear();
    }

}
