package kr.hhplus.be.server.product.domain.mapper;

import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.domain.model.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toDomain(ProductEntity productEntity){
        return Product.builder()
                .id(productEntity.getId())
                .productName(productEntity.getProductName())
                .price(productEntity.getPrice())
                .quantity(productEntity.getQuantity())
                .build();
    }

    public ProductEntity toEntity(Product product){
        return ProductEntity.builder()
                            .id(product.getId())
                            .productName(product.getProductName())
                            .price(product.getPrice())
                            .quantity(product.getQuantity())
                            .build();
    }
}
